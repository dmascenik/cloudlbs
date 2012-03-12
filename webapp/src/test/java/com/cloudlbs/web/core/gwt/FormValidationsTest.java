package com.cloudlbs.web.core.gwt;

import static org.junit.Assert.*;

import org.junit.Test;

import static com.cloudlbs.web.core.gwt.FormValidations.*;

public class FormValidationsTest {

    @Test
    public void testIsEmail() {
        assertTrue(isEmail("abc@abc.def"));
        assertTrue(isEmail("abc.def@abc.def"));
        assertTrue(isEmail("abc.def.ghi@abc.def"));
        assertTrue(isEmail("abc+def@abc.def"));
        assertTrue(isEmail("abc_def@abc.def"));
        assertTrue(isEmail("abc-def@abc.def"));
        assertTrue(isEmail("abc@abc-def.ghi"));
        assertTrue(isEmail("abc@abc.def.ghi"));

        assertFalse(isEmail("abc@abc"));
        assertFalse(isEmail(""));
        assertFalse(isEmail("  "));
        assertFalse(isEmail("abc\nabc@def.ghi "));
    }

    @Test
    public void testIsValidUsername() {
        assertTrue(isValidUsername("abcdef"));
        assertTrue(isValidUsername("123456789012345678901234567890"));
        assertTrue(isValidUsername("abc.def"));
        assertTrue(isValidUsername("abc-def"));
        assertTrue(isValidUsername("abc_de.f"));

        assertFalse(isValidUsername("abcde"));
        assertFalse(isValidUsername("1234567890123456789012345678901"));
        assertFalse(isValidUsername("ab$cde"));
    }
    
    @Test
    public void testIsValidPassword() {
        assertFalse(isValidPassword("abcdefgh"));
        assertFalse(isValidPassword("abcdef1"));
        assertFalse(isValidPassword("12345678"));
        assertFalse(isValidPassword("a1a2a3a4a5a6a7a8a9a0a1a2a3a4a5a6a7a8a9a0"));
        assertTrue(isValidPassword("abcdefg1"));
        assertTrue(isValidPassword("abcdefg1!@#$%^&*():;.+=-_"));
    }
    
}
