package com.mmall.util;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanCopier;

public class BeanUtil {
	public static void copyProperties(Object source, Object target, String... ignoreProperties) throws BeansException {
		BeanUtils.copyProperties(source, target, ignoreProperties);
	}

	public static <T> T copy(Object source, T target) throws BeansException {
		BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
		copier.copy(source, target, null);
		return target;
	}

	public static void setProperties(Map<String, Object> props, Object target, String... ignoreProperties) {
		BeanWrapper wrapper = new BeanWrapperImpl(target);
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
		Iterator<Entry<String, Object>> it = props.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			setPropertyValue(wrapper, key, value, ignoreList);
		}
	}

	private static void setPropertyValue(BeanWrapper wrapper, String propertyName, Object value, List<String> ignoreList) throws BeansException {
		if (wrapper.isWritableProperty(propertyName) && (ignoreList == null || (!ignoreList.contains(propertyName)))) {
			wrapper.setPropertyValue(propertyName, value);
		}
	}

	private static void getPropertyValue(Map<String, Object> map, BeanWrapper wrapper, String propertyName, List<String> ignoreList) throws BeansException {
		if (wrapper.isReadableProperty(propertyName) && (ignoreList == null || (!ignoreList.contains(propertyName)))) {
			Object value = wrapper.getPropertyValue(propertyName);
			map.put(propertyName, value);
		}
	}

	public static Map<String, Object> getBeanMap(Object source, String... ignoreProperties) {
		BeanWrapper wrapper = new BeanWrapperImpl(source);
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
		PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
		Map<String, Object> map = new HashMap<String, Object>();
		for (PropertyDescriptor pd : pds) {
			String propertyName = pd.getName();
			getPropertyValue(map, wrapper, propertyName, ignoreList);
		}
		return map;
	}
}