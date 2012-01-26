package com.cloudlbs.core.utils.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.test.Item;

/**
 * @author Dan Mascenik
 * 
 */
public class CriteriaQueryProcessorTest extends Assert {

	@Test
	public void testEmptyQuery() {
		String q = "";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> c = getCriteria(dc, query);
		assertEquals(0, c.size());
	}

	@Test
	public void testNullTermQuery() {
		String q = "reqString: NULL";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("reqString is null", crits.iterator().next().toString());
	}

	@Test
	public void testTermQuery() {
		String q = "reqString: test";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("reqString=test", crits.iterator().next().toString());
	}

	@Test
	public void testRangeQueryWithNumbers() {
		String q = "anInt: [21 TO 33]";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("anInt between 21 and 33", crits.iterator().next()
				.toString());
	}

	@Test
	public void testRangeQueryWithDates() {
		String q = "createDate: [* TO 1334522345123]";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("createDate<=Sun Apr 15 16:39:05 EDT 2012", crits
				.iterator().next().toString());
	}

	@Test
	public void testRangeQueryWithNowExpression() {
		String q = "createDate: [* TO NOW]";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertTrue(crits.iterator().next().toString()
				.startsWith("createDate<="));
	}

	@Test
	public void testRangeQueryWithDateString() {
		String q = "createDate: [* TO 2011-02-11T12:00:00.00Z]";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertTrue(crits.iterator().next().toString()
				.startsWith("createDate<="));
	}

	@Test
	public void testRangeQueryWithDateExpressions() {
		String q = "createDate: [NOW/DAY TO NOW/DAY-1MONTH]";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertTrue(crits.iterator().next().toString()
				.startsWith("createDate between"));
	}

	@Test
	public void testNegatedTermQuery() {
		String q = "!reqString: foo";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("(reqString<>foo)", crits.iterator().next().toString());
	}

	@Test
	public void testBooleanTermQuery() {
		String q = "(reqString: test AND guid: abc) OR reqString: foo";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("((reqString=test and guid=abc) or reqString=foo)", crits
				.iterator().next().toString());
	}

	@Test
	public void testTermQueryWithSpace() {
		String q = "reqString: \"test 0\"";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("reqString=test 0", crits.iterator().next().toString());
	}

	@Test
	public void testAllTogetherNow() {
		String q = "(reqString: test OR guid: abc) AND createDate: [1334512345123 TO *] AND (!reqString: foo)";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
	}

	@Test
	public void testWithMappings() {
		String q = "(reqString: test OR tGuid: abc) AND createDate: [1334512345123 TO *] AND (!reqString: foo)";
		Query query = new Query(q, 0, 10);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
	}

	@Test
	public void testQueryWithScopes() {
		String q = "reqString: foo";
		Query query = new Query(q, 0, 10);
		Set<String> scopes = new HashSet<String>();
		scopes.add("abc");
		scopes.add("def");
		query.setAllowedScopeGuids(scopes);
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("(reqString=foo and scope in (abc, def))", crits
				.iterator().next().toString());
	}

	@Test
	public void testQueryWithScopesAndCustomField() {
		String q = "reqString: foo";
		Query query = new Query(q, 0, 10);
		Set<String> scopes = new HashSet<String>();
		scopes.add("abc");
		scopes.add("def");
		query.setAllowedScopeGuids(scopes);
		query.setScopeFieldName("guid");
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("(reqString=foo and guid in (abc, def))", crits.iterator()
				.next().toString());
	}

	@Test
	public void testQueryWithScopesAndCustomFieldAndAlias() {
		String q = "reqString: foo";
		Query query = new Query(q, 0, 10);
		Set<String> scopes = new HashSet<String>();
		scopes.add("abc");
		scopes.add("def");
		query.setAllowedScopeGuids(scopes);
		query.setScopeFieldName("guid");
		DetachedCriteria dc = DetachedCriteria.forClass(Item.class);
		Collection<Criterion> crits = getCriteria(dc, query);
		assertNotNull(crits);
		assertEquals(1, crits.size());
		assertEquals("(reqString=foo and guid in (abc, def))", crits.iterator()
				.next().toString());
	}

	private Collection<Criterion> getCriteria(DetachedCriteria dc, Query query) {
		QueryMapper qm = new QueryMapper(Item.class);
		qm.addAlias("p", "parent.id");
		qm.addAlias("tGuid", "thing.guid");
		CriteriaQueryProcessor<Item> proc = new CriteriaQueryProcessor<Item>(
				Item.class, null, query, qm);
		return proc.toCriterion(dc);
	}
}
