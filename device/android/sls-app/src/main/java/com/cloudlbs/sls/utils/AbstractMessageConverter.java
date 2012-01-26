package com.cloudlbs.sls.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.protobuf.Message;

/**
 * @author Dan Mascenik
 * 
 * @param <M>
 * @param <T>
 */
public abstract class AbstractMessageConverter<M extends Message, T> implements
		MessageConverter<M, T> {

	private Class<M> messageClass;
	private Class<T> typeClass;

	@SuppressWarnings("unchecked")
	public AbstractMessageConverter() {

		Type[] typeArgs = ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments();
		messageClass = (Class<M>) typeArgs[0];
		typeClass = (Class<T>) typeArgs[1];

		if (messageClass == null) {
			throw new IllegalArgumentException(
					"Generic parameter M must be present");
		}
		if (typeClass == null) {
			throw new IllegalArgumentException(
					"Generic parameter T must be present");
		}
	}

	@Override
	abstract public T fromMessage(M message);

	@Override
	abstract public M toMessage(T obj);

	public byte[] toByteArray(T obj) {
		return toMessage(obj).toByteArray();
	}

	@SuppressWarnings("unchecked")
	public M fromMessage(InputStream in) throws IOException {
		M result = null;
		Method parseFromInputStream;
		try {
			parseFromInputStream = messageClass.getMethod("parseFrom",
					InputStream.class);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(
					"Unable to access the parseFrom(InputStream) method", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					"Could not find the parseFrom(InputStream) method", e);
		}

		try {
			result = (M) parseFromInputStream.invoke(null, in);
		} catch (InvocationTargetException e) {
			if (IOException.class.isAssignableFrom(e.getCause().getClass())) {
				throw (IOException) e.getCause();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not parse protobuf", e);
		}
		return result;
	}

	protected Class<M> getMessageClass() {
		return messageClass;
	}

	protected Class<T> getTypeClass() {
		return typeClass;
	}

}
