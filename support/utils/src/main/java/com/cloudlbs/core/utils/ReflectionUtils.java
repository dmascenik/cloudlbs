package com.cloudlbs.core.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;

/**
 * @author Dan Mascenik
 * 
 */
public class ReflectionUtils {

	private static HashMap<Class<?>, HashMap<String, Class<?>>> fieldTypesCache = new HashMap<Class<?>, HashMap<String, Class<?>>>();
	private static HashMap<Class<?>, HashMap<String, Field>> fieldsCache = new HashMap<Class<?>, HashMap<String, Field>>();

	public static Class<?> getJavaFieldType(Class<?> clz, String field) {
		return getJavaFieldType(clz, field, false);
	}

	/**
	 * If failing silently, returns null when the field doesn't exist. Returns
	 * Object if the type is not parameterized.
	 * 
	 * @param clz
	 * @param field
	 * @param index
	 *            the index of the parameter type to return
	 * @param failSilently
	 * @return
	 */
	public static Class<?> getJavaFieldGenericType(Class<?> clz,
			String fieldName, int index, boolean failSilently) {
		initializeCache(clz);
		Field field = fieldsCache.get(clz).get(fieldName);
		if (field == null) {
			if (!failSilently) {
				throw new IllegalArgumentException("No field \"" + field
						+ "\" for " + clz);
			} else {
				return null;
			}
		}
		Type t = field.getGenericType();
		if (t.getClass() == Class.class) {
			if (!failSilently) {
				throw new IllegalArgumentException(fieldName + " of class "
						+ clz + " is not parameterized");
			} else {
				return Object.class;
			}
		}
		ParameterizedType type = (ParameterizedType) field.getGenericType();
		return (Class<?>) type.getActualTypeArguments()[index];
	}

	public static Class<?> getJavaFieldType(Class<?> clz, String field,
			boolean failSilently) {
		initializeCache(clz);
		Class<?> c = fieldTypesCache.get(clz).get(field);
		if (c == null && !failSilently) {
			throw new IllegalArgumentException("No field \"" + field
					+ "\" for " + clz);
		}
		return c;
	}

	private static synchronized void initializeCache(Class<?> clz) {
		if (!fieldTypesCache.containsKey(clz)) {
			HashMap<String, Class<?>> fieldTypes = new HashMap<String, Class<?>>();
			HashMap<String, Field> fields = new HashMap<String, Field>();
			/*
			 * Load all the fields and their types into cache
			 */
			Class<?> c = clz;
			while (!c.equals(Object.class)) {
				for (Field f : c.getDeclaredFields()) {
					if (!fieldTypes.containsKey(f.getName())) {
						fieldTypes.put(f.getName(), f.getType());
						fields.put(f.getName(), f);
					}
				}
				c = c.getSuperclass();
			}
			fieldTypesCache.put(clz, fieldTypes);
			fieldsCache.put(clz, fields);
		}
	}

	public static void setFieldValue(Object target, Object value,
			String fieldName) {
		Field field = org.springframework.util.ReflectionUtils.findField(
				target.getClass(), fieldName);
		field.setAccessible(true);
		org.springframework.util.ReflectionUtils.setField(field, target, value);
	}

	public static Method getSetter(Class<?> clz, String fieldName,
			Class<?> setsType) {
		String setter = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		return org.springframework.util.ReflectionUtils.findMethod(clz, setter,
				setsType);
	}

	public static Method getGetter(Class<?> clz, String fieldName) {
		String getter = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		return org.springframework.util.ReflectionUtils.findMethod(clz, getter);
	}

	public static Method getGetter(Class<?> clz, String fieldName,
			Class<?>[] params) {
		String getter = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		return org.springframework.util.ReflectionUtils.findMethod(clz, getter,
				params);
	}

	/**
	 * Attempts to set the provided value on the target object's field. If
	 * value's type and the field type do not match, this will attempt to coerce
	 * the value into the necessary type.
	 * 
	 * <br/>
	 * <br/>
	 * <code>String</code> --&gt; any number format or a <code>Date</code> using
	 * {@link DateParser}<br/>
	 * <code>Long</code> --&gt; a <code>Date</code><br/>
	 * 
	 * @see #coerceType(Object, Class);
	 * 
	 * @param target
	 * @param value
	 * @param fieldName
	 */
	public static void setValue(Object target, Object value, String fieldName,
			boolean failSilently) {
		Class<?> clz = target.getClass();
		initializeCache(clz);
		Class<?> fieldType = getJavaFieldType(clz, fieldName, failSilently);
		if (fieldType == null && failSilently) {
			return;
		}
		Object coercedValue = coerceType(value, fieldType);

		// Set the value on the target
		Method setter = getSetter(clz, fieldName, fieldType);
		if (setter == null) {
			if (!failSilently) {
				throw new RuntimeException("No setter for " + fieldName
						+ " on " + clz);
			} else {
				return;
			}
		}
		try {
			setter.invoke(target, coercedValue);
		} catch (Exception e) {
			throw new RuntimeException("Could not set value for " + fieldName
					+ " on " + clz, e);
		}

	}

	public static void setValue(Object target, Object value, String fieldName) {
		setValue(target, value, fieldName, false);
	}

	public static Object getValue(Object target, String fieldName,
			boolean failSilently) {
		Class<?> clz = target.getClass();
		initializeCache(clz);
		Method getter = getGetter(clz, fieldName);
		if (getter == null) {
			if (!failSilently) {
				throw new RuntimeException("No getter for " + fieldName
						+ " on " + clz);
			} else {
				return null;
			}
		}
		Object result;
		try {
			result = getter.invoke(target, new Object[0]);
		} catch (Exception e) {
			throw new RuntimeException("Could not get value for " + fieldName
					+ " from " + clz, e);
		}
		return result;
	}

	public static Object getValue(Object target, String fieldName) {
		return getValue(target, fieldName, false);
	}

	public static Object coerceType(Object value, Class<?> targetType) {

		if (value == null) {
			return null;
		}

		Class<?> vClz = value.getClass();
		Object coercedValue = value;
		if (!targetType.isAssignableFrom(vClz)) {
			// Need to coerce

			if (String.class.isAssignableFrom(vClz)) {

				// Handle if the value is a string
				String vStr = (String) value;

				if (targetType == Integer.class || targetType == int.class) {
					coercedValue = Integer.valueOf(vStr);
				} else if (targetType == Long.class || targetType == long.class) {
					coercedValue = Long.valueOf(vStr);
				} else if (targetType == Float.class
						|| targetType == float.class) {
					coercedValue = Float.valueOf(vStr);
				} else if (targetType == Boolean.class
						|| targetType == boolean.class) {
					coercedValue = Boolean.valueOf(vStr);
				} else if (targetType == Double.class
						|| targetType == double.class) {
					coercedValue = Double.valueOf(vStr);
				} else if (targetType == Date.class) {

					try {
						// See if it's just a long
						coercedValue = new Date(Long.valueOf(vStr));
					} catch (NumberFormatException e) {
						// Not that - try a standard date string
						vStr = vStr.toUpperCase();
						coercedValue = new DateParser().parseMathLenient(null,
								vStr);
					}
				} else if (targetType == String.class) {
					coercedValue = vStr;
				} else {
					throw new IllegalArgumentException(
							"Could not coerce String to " + targetType);
				}
			} else if (Long.class.isAssignableFrom(vClz)) {

				// Handle if the value is a Long
				Long vLong = (Long) value;

				if (targetType == Long.class || targetType == long.class) {
					// Sometimes a long is just a long
					coercedValue = vLong;
				} else if (targetType == Date.class) {
					coercedValue = new Date(vLong);
				} else {
					throw new IllegalArgumentException(
							"Could not coerce Long to " + targetType);
				}

			} else if (Date.class.isAssignableFrom(vClz)) {

				// Handle if the value is a Date
				Date vDate = (Date) value;

				if (targetType == Long.class || targetType == long.class) {
					coercedValue = new Long(vDate.getTime());
				} else {
					throw new IllegalArgumentException(
							"Could not coerce Date to " + targetType);
				}

			} else {

				if (targetType == String.class) {
					coercedValue = value.toString();
				} else {
					// Take it as it is and hope for the best...
					// It might be converting a Double to a double, e.g.
				}

			}
		}
		return coercedValue;
	}

	/**
	 * Scans a package for classes.
	 */
	public static List<Class<?>> getClassesForPackage(String basePackage)
			throws IOException, ClassNotFoundException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
				resourcePatternResolver);

		List<Class<?>> candidates = new ArrayList<Class<?>>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ resolveBasePackage(basePackage) + "/" + "**/*.class";
		Resource[] resources = resourcePatternResolver
				.getResources(packageSearchPath);
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader metadataReader = metadataReaderFactory
						.getMetadataReader(resource);
				if (isCandidate(metadataReader)) {
					candidates.add(Class.forName(metadataReader
							.getClassMetadata().getClassName()));
				}
			}
		}
		return candidates;
	}

	private static String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils
				.resolvePlaceholders(basePackage));
	}

	/**
	 * Can specify candidates by annotation type
	 */
	private static boolean isCandidate(MetadataReader metadataReader)
			throws ClassNotFoundException {
		// try {
		// Class<?> c = Class.forName(metadataReader.getClassMetadata()
		// .getClassName());
		// if (c.getAnnotation(XmlRootElement.class) != null) {
		// return true;
		// }
		// } catch (Throwable e) {
		// }
		// return false;
		return true;
	}

	/**
	 * Returns any fields of the provided {@link Message} class that are
	 * extensions.
	 * 
	 * @param mClz
	 * @return
	 */
	public static List<GeneratedMessage.GeneratedExtension<?, ?>> getProtobufExtensions(
			Class<? extends Message> mClz) throws IllegalAccessException {
		List<GeneratedMessage.GeneratedExtension<?, ?>> extensions = new ArrayList<GeneratedMessage.GeneratedExtension<?, ?>>();

		Field[] fields = mClz.getFields();
		for (Field f : fields) {
			if (GeneratedMessage.GeneratedExtension.class.isAssignableFrom(f
					.getType())) {
				GeneratedMessage.GeneratedExtension<?, ?> ext = (GeneratedMessage.GeneratedExtension<?, ?>) f
						.get(null);
				extensions.add(ext);
			}
		}

		return extensions;
	}
}
