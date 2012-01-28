package com.cloudlbs.web.shared.validation;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Dan Mascenik
 * 
 */
public class UserFieldsValidatorTest extends Assert {

	@Test
	public void testIsValidUsername() {
		assertFalse(UserFieldsValidator.isValidUsername("abc"));
		assertFalse(UserFieldsValidator
				.isValidUsername("abcdefghijklmnopqrstuvwxyz"));
		assertFalse(UserFieldsValidator.isValidUsername(""));
		assertFalse(UserFieldsValidator.isValidUsername("                "));
		assertFalse(UserFieldsValidator.isValidUsername("abc def"));
		assertFalse(UserFieldsValidator.isValidUsername("abc.def"));
		assertFalse(UserFieldsValidator.isValidUsername("abc_def"));
		assertFalse(UserFieldsValidator.isValidUsername("abc-def"));
		assertFalse(UserFieldsValidator.isValidUsername("abc!def"));
		assertTrue(UserFieldsValidator.isValidUsername("abcdef"));
		assertTrue(UserFieldsValidator.isValidUsername("abcdef123"));
		assertTrue(UserFieldsValidator.isValidUsername("123456"));
	}

	@Test
	public void testIsValidPassword() {
		assertTrue(UserFieldsValidator.isValidPassword("abc123"));
		assertTrue(UserFieldsValidator.isValidPassword("abc.123!"));
		assertTrue(UserFieldsValidator.isValidPassword("@#$%^&"));
		assertFalse(UserFieldsValidator.isValidPassword(""));
		assertFalse(UserFieldsValidator.isValidPassword("                "));
		assertFalse(UserFieldsValidator.isValidPassword("asdfasdfasdf"));
		assertFalse(UserFieldsValidator.isValidPassword("abc"));
		assertFalse(UserFieldsValidator
				.isValidPassword("abcdefghhijklmnopqrstuvwxyz"));
		assertFalse(UserFieldsValidator
				.isValidPassword("abcdefghhijklmnopqrstuvwxyz123"));
		assertFalse(UserFieldsValidator.isValidPassword("1234567890"));
	}

	@Test
	public void testIsValidEmail() {
		assertTrue(UserFieldsValidator.isValidEmail("abc@def.com"));
		assertTrue(UserFieldsValidator.isValidEmail("abc@abc.def.com"));
		assertTrue(UserFieldsValidator.isValidEmail("abc@abc.def.asdf"));
		assertTrue(UserFieldsValidator.isValidEmail("abc.123@def.com"));
		assertTrue(UserFieldsValidator.isValidEmail("abc-123@def-us.com"));
		assertTrue(UserFieldsValidator.isValidEmail("abc_123@def.com"));
		assertTrue(UserFieldsValidator.isValidEmail("abc_123@def.co"));

		assertFalse(UserFieldsValidator.isValidEmail("abc"));
		assertFalse(UserFieldsValidator.isValidEmail("abc@def"));
		assertFalse(UserFieldsValidator.isValidEmail("abc@def.abcdef"));
		assertFalse(UserFieldsValidator.isValidEmail("abc@def.a"));
		assertFalse(UserFieldsValidator.isValidEmail("ab c@def.com"));
		assertFalse(UserFieldsValidator.isValidEmail("abc@d ef.com"));
		assertFalse(UserFieldsValidator.isValidEmail("abc@def.c om"));
		assertFalse(UserFieldsValidator.isValidEmail("abc@def.123"));
		assertFalse(UserFieldsValidator.isValidEmail("a!bc@def.123"));
	}

	@Test
	public void testIsValidText() {
		assertTrue(UserFieldsValidator.isValidText(null));
		assertTrue(UserFieldsValidator.isValidText(""));
		assertTrue(UserFieldsValidator.isValidText("abc"));
		assertTrue(UserFieldsValidator.isValidText("a  bc    "));
		assertTrue(UserFieldsValidator
				.isValidText("a  bc    asdfa124312341324"));
		assertFalse(UserFieldsValidator.isValidText("joe!"));
	}

}
