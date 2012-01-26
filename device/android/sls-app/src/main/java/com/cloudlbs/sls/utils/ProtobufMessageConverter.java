package com.cloudlbs.sls.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

/**
 * Simply creating an instance of this class (even an anonymous one)
 * automatically adds it to the converter registry. Note that there can only be
 * one converter for a given protobuf message or type. Re-instantiating
 * converters will overwrite them in the converter registry.
 * 
 * @author Dan Mascenik
 * 
 * @param <M>
 * @param <T>
 */
public abstract class ProtobufMessageConverter<M extends Message, T> extends
		AbstractMessageConverter<M, T> {

	@SuppressWarnings("rawtypes")
	private static HashMap<Class, ProtobufMessageConverter> messageConverters = new HashMap<Class, ProtobufMessageConverter>();
	@SuppressWarnings("rawtypes")
	private static HashMap<Class, ProtobufMessageConverter> typeConverters = new HashMap<Class, ProtobufMessageConverter>();

	public ProtobufMessageConverter() {
		super();
		messageConverters.put(getMessageClass(), this);
		typeConverters.put(getTypeClass(), this);
	}

	/**
	 * Converts an object to a protobuf message. If no
	 * {@link ProtobufMessageConverter} exists in the registry that can convert
	 * the object type, an {@link IllegalArgumentException} is thrown.
	 * 
	 * @param obj
	 */
	public static Message convertToMessage(Object obj) {
		Class<?> typeClass = obj.getClass();
		ProtobufMessageConverter<?, ?> conv = typeConverters.get(typeClass);
		if (conv == null) {
			throw new IllegalArgumentException("No converter for type "
					+ typeClass.getName());
		}
		return conv.toMessageAsObject(obj);
	}

	/**
	 * Converts a protobuf message to an object. If no
	 * {@link ProtobufMessageConverter} exists in the registry that can convert
	 * the message type, an {@link IllegalArgumentException} is thrown.
	 * 
	 * @param msg
	 */
	@SuppressWarnings("unchecked")
	public static <TT> TT convertToObject(Message msg, Class<TT> objectClass) {
		Class<?> messageClass = msg.getClass();
		ProtobufMessageConverter<?, ?> conv = messageConverters
				.get(messageClass);
		if (conv == null) {
			throw new IllegalArgumentException("No converter for type "
					+ messageClass.getName());
		}
		return (TT) conv.fromMessageAsObject(msg);
	}

	/**
	 * Parses the bytes using the named {@link Message} class. If the message
	 * type is unrecognized, an exception is thrown.
	 * 
	 * @param bytes
	 * @param msgClassName
	 */
	public static Message parseMessageBytes(byte[] bytes, String msgClassName) {
		Class<?> msgClass;
		try {
			msgClass = Class.forName(msgClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("No such message class "
					+ msgClassName, e);
		}

		// Parse the bytes into a message
		Message msg;
		try {
			Method parseFrom = msgClass.getMethod("parseFrom", byte[].class);
			msg = (Message) parseFrom.invoke(null, bytes);
		} catch (Exception e) {
			throw new RuntimeException("Could not parse protobuf bytes", e);
		}

		return msg;
	}

	@SuppressWarnings("unchecked")
	private Object fromMessageAsObject(Message message) {
		return fromMessage((M) message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public M toMessage(T obj) {
		return (M) toMessageAsObject(obj);
	}

	@SuppressWarnings("unchecked")
	private Message toMessageAsObject(Object obj) {
		Method newBuilder;
		Builder b;
		try {
			newBuilder = getMessageClass().getMethod("newBuilder",
					new Class<?>[0]);
			b = (Builder) newBuilder.invoke(null, new Object[0]);
		} catch (Exception e) {
			throw new RuntimeException(
					"Could not call newBuilder() for message type", e);
		}

		/*
		 * Copy all the fields over, skipping anything that is not set.
		 */
		List<FieldDescriptor> fds = b.getDescriptorForType().getFields();
		for (FieldDescriptor fd : fds) {
			if (fd.isRepeated()) {
				copyRepeatedFieldToMessage(obj, b, fd);
			} else {
				copySingularFieldToMessage(obj, b, fd);
			}
		}

		return (M) b.build();
	}

	@Override
	public T fromMessage(M message) {

		/*
		 * Construct the entity object
		 */
		T entity;
		try {
			Constructor<T> constructor = getTypeClass().getConstructor(
					new Class<?>[0]);
			entity = constructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Could not instantiate type "
					+ getTypeClass(), e);
		}

		/*
		 * Copy all the fields over, skipping anything that is not set and
		 * doesn't have a default.
		 */
		List<FieldDescriptor> fds = message.getDescriptorForType().getFields();
		for (FieldDescriptor fd : fds) {
			if (fd.isRepeated()) {
				if (message.getRepeatedFieldCount(fd) > 0) {
					copyRepeatedFieldToType(message, entity, fd);
				}
			} else if (message.hasField(fd) || fd.hasDefaultValue()) {
				copySingularFieldToType(message, entity, fd);
			} else {
				// unset field with no default - skip
			}
		}
		return entity;
	}

	/**
	 * Copy all the values in the repeated field to a Collection on the entity,
	 * skipping silently if there is no related field on the entity. The entity
	 * field name is expected to be the simple pluralized form of the repeated
	 * field name; e.g. item[s]
	 * 
	 * @param message
	 * @param entity
	 * @param fd
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void copyRepeatedFieldToType(M message, T entity, FieldDescriptor fd) {
		String fieldName = fd.getName() + "s";
		Collection col = (Collection) ReflectionUtils.getValue(entity,
				fieldName, true);
		if (col == null) {
			// No getter for the collection or field doesn't exist - skip
			return;
		}
		Class<?> targetType = ReflectionUtils.getJavaFieldGenericType(
				entity.getClass(), fieldName, 0, true);

		for (int i = 0; i < message.getRepeatedFieldCount(fd); i++) {
			Object value = message.getRepeatedField(fd, i);
			Object coercedValue = coerceType(value, targetType);
			col.add(coercedValue);
		}
	}

	/**
	 * Copy the field, skipping silently if there is no related field or setter
	 * on the message
	 * 
	 * @param entity
	 * @param message
	 * @param fd
	 */
	@SuppressWarnings("rawtypes")
	private void copyRepeatedFieldToMessage(Object entity, Builder b,
			FieldDescriptor fd) {
		String fieldName = fd.getName() + "s";
		Collection col = (Collection) ReflectionUtils.getValue(entity,
				fieldName, true);
		if (col == null) {
			// No values to copy
			return;
		}

		Class<?> targetType = ReflectionUtils.getGetter(getMessageClass(),
				fd.getName(), new Class<?>[] { int.class }).getReturnType();

		if (targetType == null) {
			// no target field - skip
			return;
		}

		String adderName = "add" + fd.getName().substring(0, 1).toUpperCase()
				+ fd.getName().substring(1);
		Method adder;
		try {
			adder = b.getClass().getMethod(adderName, targetType);
		} catch (Exception e1) {
			throw new RuntimeException("Could not find method " + adderName
					+ " on " + getMessageClass());
		}

		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			Object value = iter.next();
			Object coercedValue = coerceType(value, targetType);
			try {
				adder.invoke(b, coercedValue);
			} catch (Exception e) {
				throw new RuntimeException("Could not set value on message "
						+ getMessageClass() + " with " + adder.getName());
			}
		}
	}

	/**
	 * Copy the field, skipping silently if there is no related field or setter
	 * on the entity
	 * 
	 * @param message
	 * @param entity
	 * @param fd
	 */
	private void copySingularFieldToType(M message, T entity, FieldDescriptor fd) {
		Object value = message.getField(fd);
		Class<?> targetType = ReflectionUtils.getJavaFieldType(
				entity.getClass(), fd.getName(), true);
		if (targetType == null) {
			// no target field - skip
			return;
		}

		Object coercedType = coerceType(value, targetType);
		ReflectionUtils.setValue(entity, coercedType, fd.getName());
	}

	/**
	 * Copy the field, skipping silently if there is no related field or setter
	 * on the message
	 * 
	 * @param entity
	 * @param message
	 * @param fd
	 */
	private void copySingularFieldToMessage(Object entity, Builder b,
			FieldDescriptor fd) {
		Object value = ReflectionUtils.getValue(entity, fd.getName(), true);
		if (value == null) {
			// No value to copy
			return;
		}
		Class<?> targetType = ReflectionUtils.getGetter(getMessageClass(),
				fd.getName()).getReturnType();

		if (targetType == null) {
			// no target field - skip
			return;
		}

		Object coercedValue = coerceType(value, targetType);
		Method setter = ReflectionUtils.getSetter(b.getClass(), fd.getName(),
				targetType);
		try {
			setter.invoke(b, coercedValue);
		} catch (Exception e) {
			throw new RuntimeException("Could not set value on message "
					+ getMessageClass() + " with " + setter.getName());
		}
	}

	/**
	 * If the value is a {@link Message}, convert it using a registered
	 * {@link MessageConverter}. Otherwise, fall back to
	 * {@link ReflectionUtils#coerceType(Object, Class)}
	 * 
	 * @param value
	 * @param targetType
	 * @return
	 */
	private Object coerceType(Object value, Class<?> targetType) {
		if (value == null) {
			return null;
		}
		Class<?> vClz = value.getClass();
		Object coercedType;
		/*
		 * May have to convert another protobuf
		 */
		if (Message.class.isAssignableFrom(vClz)) {
			ProtobufMessageConverter<?, ?> pmc = messageConverters.get(vClz);
			if (pmc == null) {
				throw new RuntimeException("No converter for message "
						+ "type: " + vClz);
			}
			coercedType = pmc.fromMessageAsObject((Message) value);
		} else {
			ProtobufMessageConverter<?, ?> pmc = typeConverters.get(vClz);
			if (pmc != null) {
				coercedType = pmc.toMessageAsObject(value);
			} else {
				coercedType = ReflectionUtils.coerceType(value, targetType);
			}
		}
		return coercedType;
	}

}
