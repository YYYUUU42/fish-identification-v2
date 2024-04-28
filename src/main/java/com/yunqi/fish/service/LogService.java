package com.yunqi.fish.service;

import com.yunqi.fish.model.vo.AlgorithmVo;

public interface LogService {

    /**
     * 记录操作日志
     *
     * @param openid
     * @param algorithmVo
     */
    void saveLog(String openid, AlgorithmVo algorithmVo);
}
