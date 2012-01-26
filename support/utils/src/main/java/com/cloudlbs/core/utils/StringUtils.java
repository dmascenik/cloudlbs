package com.cloudlbs.core.utils;

/**
 * @author Dan Mascenik
 * 
 */
public class StringUtils {

	public static boolean isBlank(String in) {
		return (in == null || in.trim().length() == 0);
	}

}
