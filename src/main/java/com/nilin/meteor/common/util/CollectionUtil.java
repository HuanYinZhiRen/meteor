package com.nilin.meteor.common.util;

import org.springframework.validation.support.BindingAwareModelMap;

import java.util.*;

/**
 * 集合之间的转换
 * 
 * @author Administrator
 * 
 */
public class CollectionUtil {

	/**
	 * Map转数组
	 * 
	 * @param map
	 * @return
	 */
	public Object[] mapToArray(Map map) {
		Collection collections = map.values();
		Object[] parameters = new Object[map.size()];
		parameters = collections.toArray(parameters);

		return parameters;
	}

	/**
	 * 分割字符串转Map 例如 a,1,b,2,c,3,4转换成Map a=1,b=2,c=3,default=4 的map
	 * 
	 * @return
	 */
	public Map<String,String> splitStringToMap(String splitString, String split) {
		Map<String,String> splitMap = new HashMap<String,String>();
		if (ThreadUtils.getRTex().isAllNotEmpty(splitString, split)) {
			String[] splitStrs = splitString.split(split);
			int length = splitStrs.length;
			String defaultValue = null;
			if (splitStrs.length % 2 != 0) {
				defaultValue = splitStrs[length - 1];
				length--;
			}
			for (int i = 0; i < length; i = i + 2) {
				String key = splitStrs[i];
				String value = splitStrs[i+1];
				if (ThreadUtils.getRTex().isNotEmpty(key)) {
					splitMap.put(key, value);
				}
			}
			if (ThreadUtils.getRTex().isNotEmpty(defaultValue)) {
				splitMap.put("defaultValue", defaultValue);
			}
		}
		return splitMap;
	}
	
	/**
	 * List 转数组
	 * 
	 * @param oList
	 * @return
	 */
	public Object[] List2Array(List oList) {
		Object[] oArray = oList.toArray(new Object[] {});
		return oArray;
	}

	/**
	 * Set 转数组
	 * 
	 */
	public Object[] Set2Array(Set oSet) {
		Object[] oArray = oSet.toArray(new Object[] {});
		return oArray;
	}

	/**
	 * Set 转List
	 * 
	 */
	public <T extends Object> List<T> Set2List(Set<T> oSet) {
		List<T> tList = new ArrayList<T>(oSet);
		return tList;
	}

	/**
	 * 数组转 转List
	 * 
	 * @param tArray
	 * @return
	 */
	public <T extends Object> List<T> Array2List(T[] tArray) {
		List<T> tList = Arrays.asList(tArray);
		return tList;
	}

	/**
	 * List 转 Set
	 * 
	 * @param tList
	 * @return
	 */
	public <T extends Object> Set<T> List2Set(List<T> tList) {
		Set<T> tSet = new HashSet<T>(tList);
		return tSet;
	}

	/**
	 * 数组转Set
	 * 
	 * @param tArray
	 * @return
	 */
	public <T extends Object> Set<T> Array2Set(T[] tArray) {
		Set<T> tSet = new HashSet<T>(Arrays.asList(tArray));
		return tSet;
	}

	/**
	 * Map 转 List
	 * 
	 * @param map
	 * @return
	 */
	public List MapToList(Map map) {
		Collection collections = map.values();
		List parameters = new ArrayList(map.size());
		for (Object obj : collections) {
			parameters.add(obj);
		}
		return parameters;
	}

	/**
	 * Map转BindingAwareModelMap类型Map
	 * 
	 * @param bindingAwareModelMap
	 * @param paramMap
	 * @return
	 */
	public BindingAwareModelMap mapToBindingAwareModelMap(BindingAwareModelMap bindingAwareModelMap, Map paramMap) {
		if (null == bindingAwareModelMap) {
			bindingAwareModelMap = new BindingAwareModelMap();
		}
		Set<String> set = paramMap.keySet();
		for (String key : set) {
			Object value = paramMap.get(key);
			bindingAwareModelMap.put(key, value);
		}
		return bindingAwareModelMap;
	}

	

	/**
	 * List 去重添加
	 * 
	 * @param list
	 * @param tempList
	 * @return
	 */
	public List uniqAddAll(List list, List tempList) {
		if (null != list && list.size() > 0 && null != tempList && tempList.size() > 0) {
			for (Object obj : tempList) {
				if (!list.contains(obj)) {
					list.add(obj);
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取subList
	 * @param list 需要的list
	 * @param startindex 开始索引
	 * @param length 长度
	 * @return
	 */
	public List subList(List list,int startindex,int length){
		List subList = null;
		if(null!=list&&list.size()>0){
			int endIndex = startindex+length;
			if(endIndex>list.size()){
				endIndex = list.size();
			}
			subList = list.subList(startindex, endIndex);
		}
		return subList;
	}

}
