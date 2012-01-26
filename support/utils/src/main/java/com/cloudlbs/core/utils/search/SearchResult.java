package com.cloudlbs.core.utils.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchResult<T> implements Serializable {

	private static final long serialVersionUID = -8815002881118689080L;

	private Query query;

	private int queryTime;

	private int totalResults;

	private List<T> values = new ArrayList<T>();

	public SearchResult(List<T> values, Query query, int queryTime,
			int totalResults) {
		this.values.addAll(values);
		this.query = query;
		this.queryTime = queryTime;
		this.totalResults = totalResults;
	}

	public Query getQuery() {
		return query;
	}

	public int getQueryTime() {
		return queryTime;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public List<T> getValues() {
		return values;
	}

}
