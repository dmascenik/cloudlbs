package com.cloudlbs.core.utils.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.cloudlbs.core.utils.dao.CriteriaQueryProcessor;
import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.core.utils.dao.QueryMapper;
import com.cloudlbs.core.utils.domain.DatabaseEntity;
import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;

/**
 * @author Dan Mascenik
 * 
 * @param <T>
 *            An {@link Entity} type
 */
public abstract class JpaDaoGenericService<T extends DatabaseEntity, ID extends Serializable>
		implements GenericService<T> {

	protected JpaGenericDao<T, ID> entityDao;

	public JpaDaoGenericService(JpaGenericDao<T, ID> entityDao) {
		this.entityDao = entityDao;
	}

	/**
	 * Override this method to provide a {@link QueryMapper} with any aliases
	 * defined. The default implementation of this method simply returns null.
	 * 
	 */
	protected QueryMapper getQueryMapper() {
		return null;
	}

	/**
	 * Creates and persists an entity. The returned entity is the same instance
	 * as the entity passed in.
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public T createEntity(T entity) {
		Assert.notNull(entity, "Entity to create cannot be null");
		String guid = entity.getGuid();
		Assert.isNull(guid, "Cannot create entity with a preset GUID");
		setGuid(entity, UUID.randomUUID().toString());
		entityDao.makePersistent(entity);
		return entity;
	}

	/**
	 * Retrieves an entity for a given GUID.
	 * 
	 * @param guid
	 */
	@Override
	@Transactional
	public T retrieveEntity(String guid) {
		Assert.notNull(guid);
		Criterion c = Restrictions.eq("guid", guid);
		List<T> entities = entityDao.findByCriteria(c);
		if (entities.size() == 0) {
			throw new RuntimeException("No such "
					+ entityClass().getSimpleName() + " for GUID " + guid);
		} else if (entities.size() > 1) {
			throw new RuntimeException("Multiple instances of "
					+ entityClass().getSimpleName() + " for GUID " + guid);
		}
		return entities.get(0);
	}

	@Override
	@Transactional
	public T updateEntity(String guid, T representation,
			List<String> unmodifiedFields) {
		Assert.notNull(guid);
		Assert.notNull(representation);
		Assert.notNull(representation.getVersion(),
				"Version field must be populated for updates");
		if (unmodifiedFields == null) {
			unmodifiedFields = new ArrayList<String>();
		}
		T entity = retrieveEntity(guid);
		Assert.isTrue(entity.getVersion().equals(representation.getVersion()),
				"Value of version field does not "
						+ "match the version from the database");

		// Update any mutable fields that have changed, except version
		unmodifiedFields.add("version");
		BeanUtils.copyProperties(representation, entity,
				unmodifiedFields.toArray(new String[0]));
		return entity;
	}

	@Override
	@Transactional
	public void deleteEntity(String guid) {
		Assert.notNull(guid);
		T entity = retrieveEntity(guid);
		deleteEntity(entity);
	}

	@Transactional
	public void deleteEntity(T entity) {
		Assert.notNull(entity);
		entityDao.makeTransient(entity);
	}

	@Override
	public Class<T> entityClass() {
		return entityDao.getPersistentClass();
	}

	@Override
	@Transactional
	public SearchResult<T> search(Query query) {
		CriteriaQueryProcessor<T> processor = new CriteriaQueryProcessor<T>(
				entityClass(), entityDao.getSession(), query, getQueryMapper());
		return processor.execute();
	}

	@Override
	@Transactional
	public long count(Query query) {
		CriteriaQueryProcessor<T> processor = new CriteriaQueryProcessor<T>(
				entityClass(), entityDao.getSession(), query, getQueryMapper());
		return processor.count();
	}

	/**
	 * Sets the value for the GUID, despite being a private, immutable field.
	 * 
	 * @param entity
	 * @param guid
	 */
	private void setGuid(T entity, String guid) {
		Class<?> entityClass = entityClass();
		Field guidField = null;
		while (!entityClass.equals(Object.class)) {
			try {
				guidField = entityClass.getDeclaredField("guid");
				break;
			} catch (NoSuchFieldException e) {
				entityClass = entityClass.getSuperclass();
			}
		}

		try {
			guidField.setAccessible(true);
			guidField.set(entity, guid);
		} catch (Exception e) {
			throw new RuntimeException("Could not access \"guid\" "
					+ "field of class " + entityClass, e);
		}
	}
}
