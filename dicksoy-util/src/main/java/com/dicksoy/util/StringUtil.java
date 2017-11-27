package com.dicksoy.util;

public class StringUtil {
	
	public static boolean isEmpty(String str) {
		return null != str && !"".equals(str.trim());
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
}
