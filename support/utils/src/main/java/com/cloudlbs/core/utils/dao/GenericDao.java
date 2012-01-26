package com.cloudlbs.core.utils.dao;

import java.io.Serializable;
import java.util.List;

/**
 * A simple interface for common data access operations.
 * 
 * @author Dan Mascenik
 * 
 * @param <T>
 *            The persistent class handled by this DAO
 * @param <ID>
 *            The type to be used for the primary key
 */
public interface GenericDao<T, ID extends Serializable> {

	boolean exists(ID id);

	List<T> findAll();

	List<T> findByExample(T exampleInstance, String... excludeProperties);

	T findByExampleUnique(T exampleInstance, String... excludeProperties);

	T findById(ID id);

	T makePersistent(T entity);

	void makeTransient(T entity);
}
