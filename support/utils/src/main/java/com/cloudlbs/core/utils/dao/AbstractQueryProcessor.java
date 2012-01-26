package com.cloudlbs.core.utils.dao;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;

/**
 * @author Dan Mascenik
 * 
 */
public abstract class AbstractQueryProcessor<T> {

	private String defaultField = "text";
	private Query query;
	protected QueryMapper queryMapper;

	protected org.apache.lucene.search.Query lucQuery;

	private Class<T> persistentClass;

	/**
	 * @param persistentClass
	 * @param query
	 * @param queryMapper
	 *            if null, uses a default {@link QueryMapper}
	 */
	public AbstractQueryProcessor(Class<T> persistentClass, Query query,
			QueryMapper queryMapper) {
		this.query = query;
		this.persistentClass = persistentClass;
		if (queryMapper == null) {
			queryMapper = new QueryMapper(persistentClass);
		}
		this.queryMapper = queryMapper;

		if (query.getQ() != null && query.getQ().trim().length() > 0) {
			lucQuery = parseQuery();
		}
	}

	public abstract SearchResult<T> execute();

	public abstract int count();

	/**
	 * Converts the supplied {@link Query} into a Lucene query object that can
	 * be traversed and translated into the desired query format.
	 * 
	 * @return
	 */
	protected org.apache.lucene.search.Query parseQuery() {
		Analyzer analyzer = new KeywordAnalyzer();
		QueryParser parser = new QueryParser(Version.LUCENE_30, defaultField,
				analyzer);
		parser.setDefaultOperator(Operator.OR);
		try {
			org.apache.lucene.search.Query result = parser.parse(query.getQ());
			return result;
		} catch (Exception e) {
			log.error("Error parsing: \"" + query.getQ() + "\"");
			throw new RuntimeException(e);
		}
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public Query getQuery() {
		return query;
	}

	private Logger log = LoggerFactory.getLogger(AbstractQueryProcessor.class);

}
