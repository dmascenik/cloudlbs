package com.cloudlbs.core.utils.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;

/**
 * A JPA implementation of the {@link GenericDao} making use of the
 * <code>javax.persistence.EntityManager</code>. This class <i>requires</i> type
 * parameters, and if none are provided, an exception will be thrown on
 * instantiation. The type parameters may be inherited from a superclass.
 * 
 * @author Dan Mascenik
 * 
 * @param <T>
 *            The persistent class handled by this DAO
 * @param <ID>
 *            The type to be used for the primary key
 */
public class JpaGenericDao<T, ID extends Serializable> implements
		GenericDao<T, ID> {

	private Class<T> persistentClass;

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * Create an instance of the {@link JpaGenericDao} for the specified
	 * persistent class.
	 * 
	 * @param persistentClass
	 */
	public JpaGenericDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	/**
	 * Create an instance of the {@link JpaGenericDao}, using the first type
	 * parameters found walking up this class's inheritance chain. If no type
	 * parameters are found, an exception will be thrown on instantiation.
	 */
	public JpaGenericDao() {
		this.persistentClass = getGenericType(getClass());
	}

	/**
	 * Walk up the inheritance chain to find the type parameters. If Object is
	 * reached an there are still no parameters, an exception will be thrown.
	 * 
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Class<T> getGenericType(Class c) {
		if (c.getClass().equals(Object.class)) {
			// No parameter provided
			throw new RuntimeException(getClass()
					+ " extends parameterized type "
					+ JpaGenericDao.class.getSimpleName()
					+ ", which requires type parameters to function correctly");
		}

		Class<T> type = null;

		Type c0 = c.getGenericSuperclass();
		if (ParameterizedType.class.isAssignableFrom(c0.getClass())) {

			ParameterizedType pt = (ParameterizedType) c0;
			type = (Class<T>) pt.getActualTypeArguments()[0];

		} else {

			// try the superclass
			type = getGenericType(c.getSuperclass());
		}

		return type;
	}

	/**
	 * @return the {@link EntityManager} delegate, as a Hibernate
	 *         {@link Session}
	 */
	public Session getSession() {
		Session result = (Session) entityManager.getDelegate();
		return result;
	}

	public long count() {
		return ((Number) entityManager.createQuery(
				"select count(obj) from " + this.persistentClass.getName()
						+ " obj").getSingleResult()).longValue();
	}

	public T findById(ID id) {
		return entityManager.find(persistentClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return entityManager.createQuery(
				"select obj from " + this.persistentClass.getName() + " obj")
				.getResultList();
	}

	public T makePersistent(T entity) {
		entityManager.persist(entity);
		return entity;
	}

	public void makeTransient(T entity) {
		entityManager.remove(entity);
	}

	public void flush() {
		entityManager.flush();
	}

	public void clear() {
		entityManager.clear();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public boolean exists(ID id) {
		T entity = this.entityManager.find(persistentClass, id);
		return entity != null;
	}

	public List<T> findByCriteria(int startIndex, int maxResults,
			DetachedCriteria dCrit) {
		Criteria crit = dCrit.getExecutableCriteria(getSession());
		return findByCriteria(startIndex, maxResults, crit);
	}

	public List<T> findByCriteria(int startIndex, int maxResults,
			Criterion... criterion) {
		Criteria crit = getCriteria(criterion);
		return findByCriteria(startIndex, maxResults, crit);
	}

	public List<T> findByCriteria(int startIndex, int maxResults,
			Collection<Criterion> criterion) {
		Criteria crit = getCriteria(criterion);
		return findByCriteria(startIndex, maxResults, crit);
	}

	@SuppressWarnings("unchecked")
	private List<T> findByCriteria(int startIndex, int maxResults, Criteria crit) {
		crit.setFirstResult(startIndex);
		crit.setMaxResults(maxResults);
		return crit.list();
	}

	public List<T> findByCriteria(Criterion... criterion) {
		return findByCriteria(0, 10, criterion);
	}

	public long countByCriteria(Criterion... criterion) {
		Criteria crit = getCriteria(criterion);
		return countByCriteria(crit);
	}

	public long countByCriteria(Collection<Criterion> criteria) {
		Criteria crit = getCriteria(criteria);
		return countByCriteria(crit);
	}

	public long countByCriteria(DetachedCriteria dCrit) {
		Criteria crit = dCrit.getExecutableCriteria(getSession());
		return countByCriteria(crit);
	}

	private long countByCriteria(Criteria crit) {
		crit.setProjection(Projections.rowCount());
		return ((Long) (crit.list().get(0))).longValue();
	}

	private Criteria getCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		if (criterion != null) {
			for (Criterion c : criterion) {
				crit.add(c);
			}
		}
		return crit;
	}

	private Criteria getCriteria(Collection<Criterion> criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		if (criterion != null) {
			for (Criterion c : criterion) {
				crit.add(c);
			}
		}
		return crit;
	}

	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(DetachedCriteria dCrit) {
		return dCrit.getExecutableCriteria(getSession()).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String... excludeProperties) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperties) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		return crit.list();
	}

	@Override
	public T findByExampleUnique(T exampleInstance, String... excludeProperties) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperties) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		crit.setMaxResults(2);

		@SuppressWarnings("unchecked")
		List<T> hits = crit.list();

		if (hits.size() > 1) {
			throw new RuntimeException("more than one result found");
		}

		T result = hits.size() == 0 ? null : hits.get(0);
		return result;
	}

	/**
	 * 
	 * @return the persistent class handled by this {@link JpaGenericDao}
	 *         instance.
	 */
	public Class<T> getPersistentClass() {
		return persistentClass;
	}

}
