package com.cloudlbs.platform.core;

import junit.framework.TestCase;

import org.junit.Test;

public class ApiKeyUtilsTest extends TestCase {

	@Test
	public void testGenerateKey() {
		String keyString = ApiKeyUtils.generateKeyString(
				"com.cloudlbs.whereru", "ABCDEFG", "seed");
		assertFalse("Includes non-word characters",
				keyString.matches(".*\\W.*"));
		assertEquals("Algorithm changed!", "pbJS6bLTQD3oEieKQWl7ltrdvU",
				keyString);
	}

	@Test
	public void testIsValidKey() {
		assertTrue(ApiKeyUtils.isValid("pbJS6bLTQD3oEieKQWl7ltrdvU",
				"com.cloudlbs.whereru", "ABCDEFG", "seed"));
		assertFalse(ApiKeyUtils.isValid("pbJS6bLTQD3oEieKQWl7ltrdvU",
				"com.cloudlbs.otherapp", "ABCDEFG", "seed"));
	}

}
