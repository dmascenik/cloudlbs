package com.cloudlbs.sls.utils;

import org.junit.Test;

import com.cloudlbs.sls.utils.StringUtils;

public class StringUtilsTest extends org.junit.Assert {

	@Test
	public void testIsBlank() {
		assertTrue(StringUtils.isBlank(""));
		assertTrue(StringUtils.isBlank("  "));
		assertTrue(StringUtils.isBlank(null));
		assertFalse(StringUtils.isBlank("abc"));
	}
	
}
