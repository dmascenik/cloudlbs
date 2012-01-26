package com.cloudlbs.core.utils.dao;

import java.util.HashMap;

import com.cloudlbs.core.utils.ReflectionUtils;

/**
 * @author Dan Mascenik
 * 
 */
public class QueryMapper {

	private Class<?> clazz;

	private HashMap<String, String> termAliases = new HashMap<String, String>();
	private HashMap<String, String> joinTerms = new HashMap<String, String>();
	private HashMap<String, Boolean> isJoin = new HashMap<String, Boolean>();
	private HashMap<String, QueryMapper> joinedMappers = new HashMap<String, QueryMapper>();

	public QueryMapper(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @param field
	 */
	public String mapTerm(String field) {
		String alias = termAliases.get(field);
		if (alias != null) {
			return alias;
		}
		return field;
	}

	/**
	 * Returns an Object coerced to the desired type from a String value
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public Object mapValue(String field, String value) {
		Object result;
		if ("*".equals(value)) {
			result = null;
		} else if ("NULL".equals(value)) {
			result = null;
		} else {
			String javaField = mapTerm(field);
			Class<?> valueClass;
			if (isJoin.get(field) != null && isJoin.get(field)) {
				QueryMapper joinMapper = joinedMappers.get(field);
				String[] parts = javaField.split("[.]");
				valueClass = ReflectionUtils.getJavaFieldType(
						joinMapper.getMappedClass(), parts[1]);
			} else {
				valueClass = ReflectionUtils.getJavaFieldType(clazz, javaField);
			}
			result = ReflectionUtils.coerceType(value, valueClass);
		}
		return result;
	}

	private Class<?> getMappedClass() {
		return this.clazz;
	}

	public void addAlias(String field, String alias) {
		String[] parts = alias.split("\\.");
		if (parts.length == 1) {
			termAliases.put(field, alias);
			isJoin.put(field, false);
		} else if (parts.length == 2) {
			termAliases.put(field, alias);
			joinTerms.put(field, parts[0]);
			isJoin.put(field, true);
			Class<?> joinClass = ReflectionUtils.getJavaFieldType(clazz,
					parts[0]);
			joinedMappers.put(field, new QueryMapper(joinClass));
		} else {
			throw new IllegalArgumentException("Joins more than one level "
					+ "deep not supported: " + alias);
		}
	}

	public boolean requiresJoin(String field) {
		return (isJoin.get(field) != null && isJoin.get(field));
	}

	public String getJoinField(String field) {
		if (isJoin.get(field) == null || !isJoin.get(field)) {
			throw new IllegalArgumentException(field + " is not a join field");
		}
		return joinTerms.get(field);
	}
}
