package com.cloudlbs.core.utils.search;

import javax.persistence.Entity;

/**
 * Basic interface for services that allow you to perform searches on some kind
 * of {@link Entity}
 * 
 * @author Dan Mascenik
 * 
 * @param <T>
 *            An {@link Entity} type
 */
public interface Searchable<T> {

	public SearchResult<T> search(Query query);

	public long count(Query query);
}
