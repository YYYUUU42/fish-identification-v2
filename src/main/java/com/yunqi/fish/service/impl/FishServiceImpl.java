package com.yunqi.fish.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunqi.fish.service.FishService;
import com.yunqi.fish.mapper.FishCategoryMapper;
import com.yunqi.fish.mapper.FishImgMapper;
import com.yunqi.fish.mapper.FishInfoMapper;
import com.yunqi.fish.model.entity.FishCategory;
import com.yunqi.fish.model.entity.FishImg;
import com.yunqi.fish.model.entity.FishInfo;
import com.yunqi.fish.model.vo.CategoryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FishServiceImpl implements FishService {

	private final StringRedisTemplate stringRedisTemplate;
	private final FishCategoryMapper categoryMapper;
	private final FishInfoMapper infoMapper;
	private final FishImgMapper imgMapper;

	private final static String REDIS_FISH_CATEGORY_KEY = "fish-category";
	private final static String REDIS_FISH_INFO_KEY = "fish-info";
	private final static Long CATEGORY_TTL = 12L;

	/**
	 * 得到鱼的种类
	 *
	 * @return
	 */
	@Override
	public Map<String, List<CategoryVo>> getCategories() {
		//先查询缓存
		String categoryInfoJson = stringRedisTemplate.opsForValue().get(REDIS_FISH_CATEGORY_KEY);

		if (StringUtils.hasText(categoryInfoJson)) {
			return JSON.parseObject(categoryInfoJson, new TypeReference<Map<String, List<CategoryVo>>>() {
			});
		}

		//查出全部的分类数据
		List<FishCategory> list = categoryMapper.selectList(null);

		//获取第1层分类的信息
		List<FishCategory> level1 = categoryMapper.selectList(new QueryWrapper<FishCategory>().eq("parent_id", 0));

		//全部分类数据的封装
		Map<String, List<CategoryVo>> vo = level1.stream().collect(Collectors.toMap(k -> k.getName(), v -> {
			//获取当前的分类的下一层分类
			List<FishCategory> level2 = getParentId(list, v.getId());
			//封装上面的结果
			List<CategoryVo> category2Vos = null;
			if (level2 != null) {
				//第2层分类
				category2Vos = level2.stream().map(l2 -> {
					CategoryVo vo2 = new CategoryVo(v.getId().toString(), l2.getId().toString(), l2.getName());
					//第3层分类
					List<FishCategory> level3 = getParentId(list, l2.getId());
					if (level3 != null) {
						List<CategoryVo> category3Vos = level3.stream().map(l3 -> {
							CategoryVo vo3 = new CategoryVo(l2.getParentId().toString(), l3.getId().toString(), l3.getName());
							//第4层分类
							List<FishCategory> level4 = getParentId(list, l3.getId());
							if (level4 != null) {
								List<CategoryVo> category4Vos = level4.stream().map(l4 -> {
									CategoryVo vo4 = new CategoryVo(l3.getParentId().toString(), l4.getId().toString(), l4.getName());
									//第5层分类
									List<FishCategory> level5 = getParentId(list, l4.getId());
									if (level5 != null) {
										List<CategoryVo> category5Vos = level5.stream().map(l5 -> {
											CategoryVo vo5 = new CategoryVo(l4.getParentId().toString(), l5.getId().toString(), l5.getName());
											//第6层分类
											List<FishCategory> level6 = getParentId(list, l5.getId());
											if (level6 != null) {
												List<CategoryVo> category6Vos = level6.stream().map(l6 -> {
													Long fishId = l6.getId();
													FishImg fishImg = imgMapper.selectOne(new QueryWrapper<FishImg>().eq("fish_id", fishId));
													return new CategoryVo(l5.getParentId().toString(), l6.getId().toString(), l6.getName(), fishImg.getImgPath());
												}).collect(Collectors.toList());
												vo5.setCategoryList(category6Vos);
											}
											return vo5;
										}).collect(Collectors.toList());
										vo4.setCategoryList(category5Vos);
									}
									return vo4;
								}).collect(Collectors.toList());
								vo3.setCategoryList(category4Vos);
							}
							return vo3;
						}).collect(Collectors.toList());
						vo2.setCategoryList(category3Vos);
					}
					return vo2;
				}).collect(Collectors.toList());
			}
			return category2Vos;
		}, (k1, k2) -> k1, LinkedHashMap::new));

		stringRedisTemplate.opsForValue().set(REDIS_FISH_CATEGORY_KEY, JSON.toJSONString(vo), CATEGORY_TTL, TimeUnit.HOURS);

		return vo;
	}

	/**
	 * 得到鱼类信息
	 *
	 * @return
	 */
	@Override
	public List<FishInfo> getInfoList() {
		String infoJSON = stringRedisTemplate.opsForValue().get(REDIS_FISH_INFO_KEY);

		if (StringUtils.hasText(infoJSON)) {
			return JSONObject.parseArray(infoJSON, FishInfo.class);
		}

		List<FishInfo> infoList = infoMapper.selectList(null);

		stringRedisTemplate.opsForValue().set(REDIS_FISH_INFO_KEY, JSONObject.toJSONString(infoList), CATEGORY_TTL, TimeUnit.HOURS);
		return infoList;
	}

	/**
	 * 得到种类的父类信息
	 *
	 * @param selectList
	 * @param parent_id
	 * @return
	 */
	private List<FishCategory> getParentId(List<FishCategory> selectList, Long parent_id) {
		List<FishCategory> collect = selectList.stream()
				.filter(item -> item.getParentId().equals(parent_id))
				.collect(Collectors.toList());
		return collect;
	}
}
