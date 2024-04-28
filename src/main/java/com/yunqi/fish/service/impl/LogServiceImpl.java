package com.yunqi.fish.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunqi.fish.service.LogService;
import com.yunqi.fish.mapper.LogMapper;
import com.yunqi.fish.mapper.UserInfoMapper;
import com.yunqi.fish.model.entity.Log;
import com.yunqi.fish.model.entity.UserInfo;
import com.yunqi.fish.model.vo.AlgorithmVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final UserInfoMapper userInfoMapper;
    private final LogMapper logMapper;


    /**
     * 记录操作日志
     *
     * @param openid
     * @param algorithmVo
     */
    @Override
    public void saveLog(String openid, AlgorithmVo algorithmVo) {

        Log log = new Log();
        log.setOpenid(openid);

        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getOpenid, openid));
        String username = "";

        if (userInfo != null) {
            username = userInfo.getUsername();
        }
        log.setUsername(username);

        log.setImg(algorithmVo.getOldImgPath());

        String detail = "识别失败";
        //识别算法成功
        if (StringUtils.hasText(algorithmVo.getNewImgPath())) {
            log.setImg(algorithmVo.getNewImgPath());
            detail = "识别成功";
        }
        log.setDetail(detail);

        log.setTime(new Date(System.currentTimeMillis()));
        log.setOperation("鱼类识别");

        logMapper.insert(log);
    }
}
