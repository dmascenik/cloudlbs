package com.cloudlbs.sls.utils;

public class Assert {

	public static void notNull(Object obj) {
		notNull(obj, null);
	}
	
	public static void notNull(Object obj, String msg) {
		if (obj == null) {
			if (StringUtils.isBlank(msg)) {
				msg = "A required parameter was null";
			}
			throw new IllegalArgumentException(msg);
		}
	}
	
}
