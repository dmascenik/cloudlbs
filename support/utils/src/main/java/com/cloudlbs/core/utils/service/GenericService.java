package com.cloudlbs.core.utils.service;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;

import com.cloudlbs.core.utils.search.Searchable;

/**
 * Useful pattern for all services that provide CRUD and search capability.
 * 
 * @author Dan Mascenik
 * 
 * @param <T>
 *            An {@link Entity} type
 */
public interface GenericService<T> extends Searchable<T> {

	/**
	 * Create an entity from the given representation.
	 * 
	 */
	public T createEntity(T representation);

	public void deleteEntity(String id) throws EntityNotFoundException;

	public T retrieveEntity(String id) throws EntityNotFoundException;

	public T updateEntity(String id, T representation,
			List<String> unmodifiedFields) throws EntityNotFoundException;

	/**
	 * Returns the entity class
	 * 
	 */
	public Class<T> entityClass();

}
