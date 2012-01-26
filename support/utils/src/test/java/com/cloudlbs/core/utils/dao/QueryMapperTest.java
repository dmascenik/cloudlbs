package com.cloudlbs.core.utils.dao;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.cloudlbs.core.utils.test.Item;

/**
 * @author Dan Mascenik
 * 
 */
public class QueryMapperTest extends Assert {

	@Test
	public void testQueryMapper() {
		QueryMapper q = new QueryMapper(Item.class);

		assertEquals("abc", q.mapTerm("abc"));

		assertEquals(123, q.mapValue("anInt", "123"));
		assertEquals(new Date(1234l), q.mapValue("createDate", "1234"));
		assertEquals("abc", q.mapValue("guid", "abc"));

		try {
			q.mapValue("foo", "bar");
			fail("Should not have worked with bogus field name");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testAliasMappingTooDeep() {
		QueryMapper q = new QueryMapper(Item.class);
		try {
			q.addAlias("s", "scope.guid.foo");
			fail("Too many joins should have failed");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testAliasMapping() {
		QueryMapper q = new QueryMapper(Item.class);
		q.addAlias("t", "thing.guid");
		assertTrue(q.requiresJoin("t"));
		assertEquals("thing", q.getJoinField("t"));
		assertEquals("thing.guid", q.mapTerm("t"));

		q.addAlias("l", "reqString");
		assertFalse(q.requiresJoin("l"));
		assertEquals("reqString", q.mapTerm("l"));
	}

	@Test
	public void testAliasType() {
		QueryMapper q = new QueryMapper(Item.class);

		// Doesn't work for generic variable fields
		// q.addAlias("s", "thing.id");
		// Object value = q.mapValue("s", "123");
		// assertTrue(Long.class.isAssignableFrom(value.getClass()));

		q.addAlias("p", "parent.modDate");
		Object value = q.mapValue("p", "123456");
		assertTrue(Date.class.isAssignableFrom(value.getClass()));
	}
}
