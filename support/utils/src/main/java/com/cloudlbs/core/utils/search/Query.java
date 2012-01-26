package com.cloudlbs.core.utils.search;

import java.io.Serializable;
import java.util.Set;

public class Query implements Serializable {

	private static final long serialVersionUID = -1569286795956456298L;
	
	private long firstResult;
	private long maxResults;
	private String q;
	private String scopeGuid;
	private Sort[] sorts;
	private Set<String> allowedScopeGuids;
	private String scopeFieldName = "scope";

	public Query(String q, int firstResult, int maxResults, String scopeGuid) {
		this.q = q;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
		this.scopeGuid = scopeGuid;
	}

	public Query(String q, int firstResult, int maxResults) {
		this.q = q;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public Query(String q, int firstResult, int maxResults, String scopeGuid,
			Sort... sorts) {
		this(q, firstResult, maxResults, scopeGuid);
		this.sorts = sorts;
	}

	public Query(String q, int firstResult, int maxResults, Sort... sorts) {
		this(q, firstResult, maxResults);
		this.sorts = sorts;
	}

	public long getFirstResult() {
		return firstResult;
	}

	public long getMaxResults() {
		return maxResults;
	}

	public String getQ() {
		return q;
	}

	public String getScopeGuid() {
		return scopeGuid;
	}

	public Sort[] getSorts() {
		return sorts;
	}

	public Set<String> getAllowedScopeGuids() {
		return allowedScopeGuids;
	}

	public void setAllowedScopeGuids(Set<String> allowedScopeGuids) {
		this.allowedScopeGuids = allowedScopeGuids;
	}

	public String getScopeFieldName() {
		return scopeFieldName;
	}

	public void setScopeFieldName(String scopeFieldName) {
		this.scopeFieldName = scopeFieldName;
	}

}
