package com.yunqi.fish.service;

import com.yunqi.fish.model.entity.FishInfo;
import com.yunqi.fish.model.vo.CategoryVo;

import java.util.List;
import java.util.Map;

public interface FishService {

	/**
	 * 得到鱼的种类
	 * @return
	 */
	Map<String, List<CategoryVo>> getCategories();

	/**
	 * 得到鱼类信息
	 * @return
	 */
	List<FishInfo> getInfoList();
}
