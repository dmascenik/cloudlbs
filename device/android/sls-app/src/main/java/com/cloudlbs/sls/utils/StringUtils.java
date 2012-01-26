package com.cloudlbs.sls.utils;

public class StringUtils {

	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}
	
}
