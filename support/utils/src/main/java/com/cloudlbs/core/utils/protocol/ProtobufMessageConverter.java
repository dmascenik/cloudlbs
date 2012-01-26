package com.cloudlbs.core.utils.protocol;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.cloudlbs.core.utils.ReflectionUtils;
import com.cloudlbs.core.utils.domain.DatabaseEntity;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

/**
 * This base {@link MessageConverter} implementation uses the
 * <code>protobuf-java-format</code> library to provide conversion of protobuf
 * {@link Message}s to various other convenient, human readable formats.
 * 
 * 
 * @author Dan Mascenik
 * 
 * @param <M>
 *            A Protobuf {@link Message} type
 * @param <T>
 */
public class ProtobufMessageConverter<M extends Message, T> extends
		AbstractMessageConverter<M, T> {

	@SuppressWarnings("rawtypes")
	private static HashMap<Class, ProtobufMessageConverter> messageConverters = new HashMap<Class, ProtobufMessageConverter>();
	@SuppressWarnings("rawtypes")
	private static HashMap<Class, ProtobufMessageConverter> entityConverters = new HashMap<Class, ProtobufMessageConverter>();

	private Class<T> typeClass;

	@SuppressWarnings("unchecked")
	public ProtobufMessageConverter() {
		super();
		Type[] typeArgs = ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments();
		typeClass = (Class<T>) typeArgs[1];

		Assert.notNull(typeClass, "Generic parameter T must be present");

		messageConverters.put(getMessageClass(), this);
		entityConverters.put(getTypeClass(), this);
	}

	public ProtobufMessageConverter(Class<M> messageClass, Class<T> typeClass) {
		super(messageClass);
		this.typeClass = typeClass;

		messageConverters.put(getMessageClass(), this);
		entityConverters.put(getTypeClass(), this);
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

	/**
	 * Returns the Protobuf message class that can be converted from the
	 * provided object class, or null if there is no converter that can handle
	 * it.
	 * 
	 * @param typeClass
	 */
	public static Class<? extends Message> getMessageClass(Class<?> typeClass) {
		Class<? extends Message> mClz = null;
		ProtobufMessageConverter<?, ?> conv = entityConverters.get(typeClass);
		if (conv != null) {
			mClz = conv.getMessageClass();
		}
		return mClz;
	}

	/**
	 * Calls the {@link ProtobufMessageConverter} that handles the type. If no
	 * converter exists, an exception is thrown.
	 * 
	 * @param obj
	 */
	public static Message fromObject(Object obj) {
		ProtobufMessageConverter<?, ?> conv = entityConverters.get(obj
				.getClass());
		if (conv == null) {
			String s = "No message converter for type "
					+ obj.getClass().getName();
			log.error(s);
			throw new IllegalArgumentException(s);
		} else {
			log.debug("Found " + conv.getClass().getName());
		}
		return conv.toMessageAsObject(obj);
	}

	/**
	 * Calls the {@link ProtobufMessageConverter} that handles the message type.
	 * If no converter exists, an exception is thrown.
	 * 
	 * @param message
	 */
	public static Object toObject(Message message) {
		log.debug("Looking up converter for " + message.getClass().getName());
		ProtobufMessageConverter<?, ?> conv = messageConverters.get(message
				.getClass());
		if (conv == null) {
			String s = "No message converter for message type "
					+ message.getClass().getName();
			log.error(s);
			throw new IllegalArgumentException(s);
		} else {
			log.debug("Found " + conv.getClass().getName());
		}
		return conv.fromMessageAsObject(message);
	}

	/**
	 * Parses the bytes using the named {@link Message} class, then calls the
	 * {@link ProtobufMessageConverter} that handles the message type. If the
	 * message type is unrecognized, or no converter exists, an exception is
	 * thrown.
	 * 
	 * @param bytes
	 * @param msgClassName
	 */
	public static Object fromMessageBytes(byte[] bytes, String msgClassName) {
		Message message = parseMessageBytes(bytes, msgClassName);
		return toObject(message);
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
		FieldDescriptor guidDesc = message.getDescriptorForType()
				.findFieldByName("guid");

		/*
		 * Construct the entity object
		 */
		T entity;
		try {
			Constructor<T> constructor = getTypeClass().getConstructor(
					new Class<?>[0]);
			if (DatabaseEntity.class.isAssignableFrom(getTypeClass())
					&& guidDesc != null && message.hasField(guidDesc)) {
				/*
				 * This is a DatabaseEntity object AND it has a GUID
				 */
				constructor = getTypeClass().getConstructor(String.class);
				entity = constructor.newInstance(message.getField(guidDesc));

				/*
				 * Add the createDate if it has one
				 */
				FieldDescriptor createDateDesc = message.getDescriptorForType()
						.findFieldByName("createDate");
				if (createDateDesc != null && message.hasField(createDateDesc)) {
					ReflectionUtils.setFieldValue(entity, ReflectionUtils
							.coerceType(message.getField(createDateDesc),
									Date.class), "createDate");
				}

			} else {
				/*
				 * Just a POJO
				 */
				entity = constructor.newInstance();
			}
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
				throw new RuntimeException("Could not set "
						+ coercedValue.getClass() + " on message "
						+ getMessageClass() + " with " + adder.getName(), e);
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
			ProtobufMessageConverter<?, ?> pmc = entityConverters.get(vClz);
			if (pmc != null) {
				coercedType = pmc.toMessageAsObject(value);
			} else {
				coercedType = ReflectionUtils.coerceType(value, targetType);
			}
		}
		return coercedType;
	}

	protected Class<T> getTypeClass() {
		return typeClass;
	}

	private static Logger log = LoggerFactory
			.getLogger(ProtobufMessageConverter.class);
}
