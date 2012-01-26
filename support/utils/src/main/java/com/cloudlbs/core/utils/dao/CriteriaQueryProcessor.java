package com.cloudlbs.core.utils.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.search.Sort;

/**
 * This class is not thread safe!
 * 
 * @author Dan Mascenik
 * 
 */
public class CriteriaQueryProcessor<T> extends AbstractQueryProcessor<T> {

	private Session session;

	/**
	 * @see AbstractQueryProcessor#AbstractQueryProcessor(Class, Query,
	 *      QueryMapper)
	 * 
	 * @param persistentClass
	 * @param session
	 * @param query
	 * @param queryMapper
	 */
	public CriteriaQueryProcessor(Class<T> persistentClass, Session session,
			Query query, QueryMapper queryMapper) {
		super(persistentClass, query, queryMapper);
		this.session = session;
	}

	@Override
	public SearchResult<T> execute() {

		DetachedCriteria dc = getCriteria();
		Criteria criteria = dc.getExecutableCriteria(session);
		criteria.setMaxResults((int) getQuery().getMaxResults());
		criteria.setFirstResult((int) getQuery().getFirstResult());
		addSorts(criteria, getQuery().getSorts());

		/*
		 * Get the list and the count, timing the whole exercise
		 */
		long start = System.currentTimeMillis();

		@SuppressWarnings("unchecked")
		List<T> values = criteria.list();
		int totalResults = count();
		long end = System.currentTimeMillis();

		int queryTime = (int) (end - start);
		return new SearchResult<T>(values, getQuery(), queryTime, totalResults);
	}

	@Override
	public int count() {
		DetachedCriteria dc = getCriteria();
		Criteria crit = dc.getExecutableCriteria(session);
		crit.setProjection(Projections.rowCount());
		return ((Long) crit.list().get(0)).intValue();
	}

	protected DetachedCriteria getCriteria() {
		DetachedCriteria crit = DetachedCriteria.forClass(getPersistentClass());
		Collection<Criterion> criteria = toCriterion(crit);
		for (Criterion c : criteria) {
			crit.add(c);
		}
		return crit;
	}

	protected void addSorts(Criteria criteria, Sort... sorts) {
		if (sorts != null && sorts.length > 0) {
			for (Sort sort : sorts) {
				String field = queryMapper.mapTerm(sort.getField());
				if (Sort.Direction.Descending == sort.getDirection()) {
					criteria.addOrder(Order.desc(field));
				} else {
					criteria.addOrder(Order.asc(field));
				}
			}
		}
	}

	protected Collection<Criterion> toCriterion(DetachedCriteria crit) {
		List<Criterion> crits;
		if (lucQuery == null) {
			crits = new ArrayList<Criterion>();
		} else {
			crits = toCriterion(crit, lucQuery);
		}

		Criterion scopes = null;
		if (getQuery().getAllowedScopeGuids() != null
				&& getQuery().getAllowedScopeGuids().size() > 0) {
			/*
			 * Add scope restrictions
			 */
			scopes = handleContainsQuery(crit, getQuery().getScopeFieldName(),
					getQuery().getAllowedScopeGuids());
		}

		/*
		 * Merge everything into a conjunction
		 */
		if (scopes != null) {
			if (crits.size() == 0) {
				crits.add(scopes);
			} else {
				Assert.isTrue(crits.size() == 1);
				Criterion c = crits.get(0);
				Junction junction = Restrictions.conjunction();
				junction.add(c);
				junction.add(scopes);
				crits = new ArrayList<Criterion>();
				crits.add(junction);
			}
		}

		return crits;
	}

	protected List<Criterion> toCriterion(DetachedCriteria crit,
			org.apache.lucene.search.Query query) {
		List<Criterion> crits = new ArrayList<Criterion>();
		if (query instanceof TermQuery) {
			crits.add(handleTermQuery(crit, (TermQuery) query));
		} else if (query instanceof BooleanQuery) {
			crits.add(handleBooleanQuery(crit, (BooleanQuery) query));
		} else if (query instanceof TermRangeQuery) {
			Criterion c = handleRangeQuery(crit, (TermRangeQuery) query);
			if (c != null) {
				crits.add(c);
			}
		} else if (query instanceof PrefixQuery) {
			crits.add(handlePrefixQuery(crit, (PrefixQuery) query));
		} else {
			throw new RuntimeException("Unknown query type: "
					+ query.getClass());
		}
		return crits;
	}

	protected Criterion handleProhibitedQuery(DetachedCriteria crit,
			org.apache.lucene.search.Query query) {
		if (query instanceof TermQuery) {
			return handleProhibitedTermQuery(crit, (TermQuery) query);
		} else {
			throw new RuntimeException("Unsupported query: " + query);
		}
	}

	protected Criterion handleBooleanQuery(DetachedCriteria crit,
			BooleanQuery boolQuery) {
		List<BooleanClause> clauses = boolQuery.clauses();
		List<Criterion> subCrits = new ArrayList<Criterion>();
		int orCount = 0;
		int andCount = 0;
		for (BooleanClause clause : clauses) {
			if (clause.isRequired()) {
				andCount++;
			} else {
				orCount++;
			}

			if (clause.isProhibited()) {
				subCrits.add(handleProhibitedQuery(crit, clause.getQuery()));
			} else {
				subCrits.addAll(toCriterion(crit, clause.getQuery()));
			}
		}
		if (andCount > 0 && orCount > 0) {
			throw new RuntimeException("Mixed boolean clauses not supported!");
		}

		Junction junction;
		if (andCount > 0) {
			junction = Restrictions.conjunction();
		} else if (orCount > 0) {
			junction = Restrictions.disjunction();
		} else {
			throw new RuntimeException("No clauses in boolean expression!");
		}

		for (Criterion c : subCrits) {
			junction.add(c);
		}

		return junction;
	}

	protected Criterion handleTermQuery(DetachedCriteria crit,
			TermQuery termQuery) {
		Term term = termQuery.getTerm();
		String field = term.field();
		String javaField = queryMapper.mapTerm(term.field());
		Object value = queryMapper.mapValue(field, term.text());
		if (queryMapper.requiresJoin(field)) {
			String joinField = queryMapper.getJoinField(field);
			crit.createAlias(joinField, joinField);
		}
		if (value == null) {
			return Restrictions.isNull(javaField);
		} else {
			return Restrictions.eq(javaField, value);
		}
	}

	protected Criterion handleProhibitedTermQuery(DetachedCriteria crit,
			TermQuery termQuery) {
		Term term = termQuery.getTerm();
		String field = term.field();
		String javaField = queryMapper.mapTerm(term.field());
		Object value = queryMapper.mapValue(field, term.text());
		if (queryMapper.requiresJoin(field)) {
			String joinField = queryMapper.getJoinField(field);
			crit.createAlias(joinField, joinField);
		}
		return Restrictions.ne(javaField, value);
	}

	protected Criterion handlePrefixQuery(DetachedCriteria crit,
			PrefixQuery prefixQuery) {
		Term term = prefixQuery.getPrefix();
		String field = term.field();
		String javaField = queryMapper.mapTerm(term.field());
		Object value = queryMapper.mapValue(field, term.text());
		if (queryMapper.requiresJoin(field)) {
			String joinField = queryMapper.getJoinField(field);
			crit.createAlias(joinField, joinField);
		}
		return Restrictions.like(javaField, value + "%");
	}

	protected Criterion handleRangeQuery(DetachedCriteria crit,
			TermRangeQuery rangeQuery) {
		String field = rangeQuery.getField();
		String javaField = queryMapper.mapTerm(field);
		Object lower = queryMapper.mapValue(field, rangeQuery.getLowerTerm());
		Object upper = queryMapper.mapValue(field, rangeQuery.getUpperTerm());
		if (queryMapper.requiresJoin(field)) {
			String joinField = queryMapper.getJoinField(field);
			crit.createAlias(joinField, joinField);
		}
		Criterion result = null;
		if ((lower == null) && (upper == null)) {
			// [* TO *]
			// no constraint
		} else if (lower == null) {
			// [* TO upper]
			result = Restrictions.le(javaField, upper);
		} else if (upper == null) {
			// [ lower TO *]
			result = Restrictions.ge(javaField, lower);
		} else {
			result = Restrictions.between(javaField, lower, upper);
		}
		return result;
	}

	protected Criterion handleContainsQuery(DetachedCriteria crit,
			String field, Set<String> values) {
		String javaField = queryMapper.mapTerm(field);
		if (queryMapper.requiresJoin(field)) {
			String joinField = queryMapper.getJoinField(field);
			crit.createAlias(joinField, joinField);
		}
		return Restrictions.in(javaField, values);
	}

}
