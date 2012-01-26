package com.cloudlbs.core.utils.remote;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.util.Assert;

/**
 * @author Dan Mascenik
 * 
 */
public abstract class AbstractRemoteService<M, T> {

	private Class<M> messageClass;
	private Class<T> typeClass;

	@SuppressWarnings("unchecked")
	public AbstractRemoteService() {
		Type[] typeArgs = ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments();
		messageClass = (Class<M>) typeArgs[0];
		typeClass = (Class<T>) typeArgs[1];
		Assert.notNull(messageClass, "Parameter M must be defined");
		Assert.notNull(typeClass, "Parameter T must be defined");
	}

	public AbstractRemoteService(Class<M> messageClass, Class<T> typeClass) {
		Assert.notNull(messageClass, "Parameter M must be defined");
		Assert.notNull(typeClass, "Parameter T must be defined");
		this.messageClass = messageClass;
		this.typeClass = typeClass;
	}

	protected Class<M> getMessageClass() {
		return messageClass;
	}

	protected Class<T> getTypeClass() {
		return typeClass;
	}

}
