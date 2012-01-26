package com.cloudlbs.sls.utils;

import org.junit.Test;

import com.cloudlbs.sls.utils.Assert;

public class AssertTest extends org.junit.Assert {

	@Test
	public void testAssertNotNull() {
		try {
			Assert.notNull(null);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		try {
			Assert.notNull(null, "test");
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("test", e.getMessage());
		}

		// Should do nothing
		Assert.notNull(new Object());
		
	}

}
