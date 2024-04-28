package com.yunqi.fish.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.yunqi.fish.client.Client;
import com.yunqi.fish.service.FileService;
import com.yunqi.fish.mapper.FishInfoMapper;
import com.yunqi.fish.model.entity.FishInfo;
import com.yunqi.fish.model.vo.AlgorithmVo;
import com.yunqi.fish.service.IdentificationService;
import com.yunqi.fish.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yunqi.fish.common.constant.CommonConstant.ALGORITHM_PATH;
import static com.yunqi.fish.common.constant.RedisKeyConstant.IDENTIFICATION_REDISSON_LOCK;
import static com.yunqi.fish.common.constant.RedisKeyConstant.REDIS_FISH_LIST;

@Service
@RequiredArgsConstructor
public class IdentificationServiceImpl implements IdentificationService {

    private final RedissonClient redissonClient;
    private final FileService fileService;
    private final StringRedisTemplate stringRedisTemplate;
    private final FishInfoMapper fishInfoMapper;

    private final Logger logger = LoggerFactory.getLogger(IdentificationServiceImpl.class);


    /**
     * 鱼类识别
     *
     * @param file
     * @return
     */
    @Override
    public AlgorithmVo identification(MultipartFile file) {
        RLock lock = redissonClient.getLock(IDENTIFICATION_REDISSON_LOCK);
        AlgorithmVo vo = null;

        try {
            if (lock.tryLock(10, 5, TimeUnit.SECONDS)) {
                try {
                    String oldImgPath = fileService.imageUpload(file);

                    vo = getAlgorithmInfo();
                    vo.setOldImgPath(oldImgPath);
                } catch (Exception e) {
                    logger.error("加锁成功，当前操作出现异常：", e);
                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            logger.error("加锁失败：", e);
        }

        return vo;
    }

    /**
     * 得到算法调用后的结果
     */
    private AlgorithmVo getAlgorithmInfo() {
        AlgorithmVo vo = new AlgorithmVo();

        try {
            String exp = Client.exc("--save-txt", "--save-conf");

            // 使用Paths避免直接字符串拼接，更安全、更清晰
            String expPath = Paths.get(ALGORITHM_PATH, "detect", "runs", "detect", exp, "labels").toString();
            String imgPath = Paths.get(ALGORITHM_PATH, "detect", "runs", "detect", exp).toString();

            // 列出文件前进行路径有效性检查
            File expDir = new File(expPath);
            if (!expDir.exists() || !expDir.isDirectory()) {
                // 记录日志或进行其他适当处理
                return vo;
            }

            File[] files = expDir.listFiles();
            // 增加对文件数组为空的检查
            if (files == null || files.length == 0) {
                return vo;
            }

            List<String> lines = FileUtil.readUtf8Lines(files[0]);
            // 检查lines是否为空以及首行是否为空
            if (lines.isEmpty() || lines.get(0).isEmpty()) {
                return vo;
            }
            String[] split = lines.get(0).split(" ");

            // 假设getFishList()和get()都是安全的
            FishInfo fishList = getFishList().get(Integer.parseInt(split[0]));

            vo.setChineseName(fishList.getFishkinds());
            vo.setEnglishName(fishList.getFishlatinname());
            vo.setConfidenceDegree(split[5]);
            vo.setNewImgPath(fileService.returnImageLink(imgPath));
        } catch (Exception e) {
            logger.error("算法结果异常：", e);
        }

        return vo;
    }

    /**
     * 获得所有的鱼类信息
     *
     * @return
     */
    private List<FishInfo> getFishList() {
/*		List<FishList> fishList;
		if (stringRedisTemplate.hasKey(REDIS_FISH_LIST)) {
			String s = stringRedisTemplate.opsForValue().get(REDIS_FISH_LIST);
			fishList = JSON.parseArray(s, FishList.class);
		} else {
			List<FishInfo> fishInfos = fishInfoMapper.selectList(null);
			fishList = BeanCopyUtils.copyBeanList(fishInfos, FishList.class);
			String jsonString = JSON.toJSONString(fishList);
			stringRedisTemplate.opsForValue().set(REDIS_FISH_LIST, jsonString);
		}

		return fishList;*/

        String fishListJson = stringRedisTemplate.opsForValue().get(REDIS_FISH_LIST);
        List<FishInfo> fishList = new ArrayList<>();

        if (fishListJson != null) {
            // 如果缓存中存在对应的数据，则直接从缓存中获取
            fishList = JSONUtil.toList(fishListJson, FishInfo.class);
        } else {
            // 缓存中不存在对应数据，则从数据库中获取，并将结果存入缓存
            synchronized (this) {
                fishListJson = stringRedisTemplate.opsForValue().get(REDIS_FISH_LIST);
                if (fishListJson != null) {
                    fishList = JSONUtil.toList(fishListJson, FishInfo.class);
                } else {
                    List<FishInfo> fishInfos = fishInfoMapper.selectList(null);
                    String jsonString = JSONUtil.toJsonStr(fishInfos);;
                    stringRedisTemplate.opsForValue().set(REDIS_FISH_LIST, jsonString);
                }
            }
        }
        return fishList;
    }
}