package com.cloudlbs.core.utils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.cloudlbs.core.utils.test.Item;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;
import com.google.protobuf.GeneratedMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class ReflectionUtilsTest extends Assert {

	@Test
	public void testGetJavaType() {
		assertEquals(Date.class,
				ReflectionUtils.getJavaFieldType(Item.class, "strDate"));
	}

	@Test
	public void testGetSetter() {
		Method setter = ReflectionUtils.getSetter(Item.class, "strDate",
				Date.class);
		assertNotNull(setter);
		assertEquals("setStrDate", setter.getName());
	}

	@Test
	public void testGetGetter() {
		Method getter = ReflectionUtils.getGetter(Item.class, "strDate");
		assertNotNull(getter);
		assertEquals("getStrDate", getter.getName());
	}

	@Test
	public void testCoerceType() {
		assertEquals("123",
				ReflectionUtils.coerceType(new Integer(123), String.class));
		assertEquals(123, ReflectionUtils.coerceType("123", Integer.class));
		assertNotNull(ReflectionUtils.coerceType(new Item(), String.class));
		assertNotNull(ReflectionUtils.coerceType("NOW", Date.class));
		assertNotNull(ReflectionUtils.coerceType(1234l, Date.class));
		assertNotNull(ReflectionUtils.coerceType("1234", Date.class));
	}

	@Test
	public void testSetAndGetValues() {
		Item item = new Item();
		ReflectionUtils.setValue(item, "123", "anInt");
		assertEquals(123, ReflectionUtils.getValue(item, "anInt"));
	}

	@Test
	public void testScanPackage() throws Exception {
		List<Class<?>> classes = ReflectionUtils
				.getClassesForPackage("com.cloudlbs.platform.protocol");
		assertTrue(classes.size() > 0);
	}

	@Test
	public void testGetProtobufExtensions() throws Exception {
		List<GeneratedMessage.GeneratedExtension<?, ?>> extensions = ReflectionUtils
				.getProtobufExtensions(UserAccountMessage.class);
		assertNotNull(extensions);
		assertEquals(1, extensions.size());
		assertEquals("account.UserAccountMessage.items", extensions.get(0)
				.getDescriptor().getFullName());
	}
}
