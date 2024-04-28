package com.yunqi.fish.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunqi.fish.anno.Time;
import com.yunqi.fish.manager.RedisLimiterManager;
import com.yunqi.fish.model.vo.AlgorithmVo;
import com.yunqi.fish.service.IdentificationService;
import com.yunqi.fish.service.LogService;
import com.yunqi.fish.utils.MD5;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static com.yunqi.fish.common.constant.RedisKeyConstant.REDIS_ALREADY_IDENTIFIED_IMAGE;
import static com.yunqi.fish.common.constant.RedisKeyConstant.REDIS_LIMITER_KEY;

/**
 * 算法调用
 */
@RestController
@RequestMapping("/fish")
@RequiredArgsConstructor
@Api(tags = "算法调用")
public class IdentificationController {

    private final IdentificationService identificationService;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisLimiterManager redisLimiterManager;
    private final LogService logService;


    /**
     * 鱼类识别
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Time()
    @PostMapping("/identification")
    @ApiOperation(value = "鱼类识别")
    public JSONObject identification(HttpServletRequest request) throws Exception {
//	public JSONObject identification(MultipartFile file) throws Exception {
        request.setCharacterEncoding("utf-8");
        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;

        MultipartFile file = req.getFile("image");
        String openid = request.getParameter("openid");

        String MD5key = MD5.encrypt(file);
        String redisKey = String.format(REDIS_ALREADY_IDENTIFIED_IMAGE + "%s", MD5key);

//        if (stringRedisTemplate.hasKey(redisKey)) {
        if(false){
            String res = stringRedisTemplate.opsForValue().get(redisKey);
            AlgorithmVo algorithmVo = JSON.parseObject(res, AlgorithmVo.class);
            logService.saveLog(openid,algorithmVo);
            return JSON.parseObject(res);
        } else {
            //根据用户的 openid 进行限流操作
            redisLimiterManager.doRateLimit(String.format(REDIS_LIMITER_KEY + "%s", openid));

            AlgorithmVo vo = identificationService.identification(file);
            String res = JSON.toJSONString(vo);
            stringRedisTemplate.opsForValue().set(redisKey, res, 12, TimeUnit.HOURS);
            logService.saveLog(openid,vo);

            return JSONObject.parseObject(res);
        }
    }
}
