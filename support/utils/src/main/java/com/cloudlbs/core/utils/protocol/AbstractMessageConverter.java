package com.cloudlbs.core.utils.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.JsonFormat;
import com.google.protobuf.Message;
import com.google.protobuf.XmlFormat;

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
 *            An {@link Entity} type
 */
public abstract class AbstractMessageConverter<M extends Message, T> implements
		MessageConverter<M, T> {

	private Class<M> messageClass;

	public AbstractMessageConverter(Class<M> messageClass) {
		Assert.notNull(messageClass, "Message class cannot be null");
		this.messageClass = messageClass;
	}

	@SuppressWarnings("unchecked")
	public AbstractMessageConverter() {

		Type[] typeArgs = ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments();
		messageClass = (Class<M>) typeArgs[0];

		Assert.notNull(messageClass, "Generic parameter M must be present");
	}

	@Override
	abstract public T fromMessage(M message);

	/**
	 * This does not return repeated fields, present or not
	 */
	@Override
	public List<String> getUnsetMessageFields(M message) {
		List<FieldDescriptor> fds = message.getDescriptorForType().getFields();
		List<String> fields = new ArrayList<String>();
		for (FieldDescriptor fd : fds) {
			if (fd.isRepeated()) {
				fields.add(fd.getName() + "s");
			} else {
				if (!message.hasField(fd)) {
					fields.add(fd.getName());
				}
			}
		}
		return fields;
	}

	public String toJson(M message) {
		return JsonFormat.printToString(message);
	}

	public String toJson(T obj) {
		return toJson(toMessage(obj));
	}

	@Override
	abstract public M toMessage(T obj);

	public String toXml(M message) {
		return XmlFormat.printToString(message);
	}

	public String toXml(T obj) {
		return toXml(toMessage(obj));
	}

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
			throw new HttpMessageNotReadableException(
					"Unable to access the parseFrom(InputStream) method", e);
		} catch (NoSuchMethodException e) {
			throw new HttpMessageNotReadableException(
					"Could not find the parseFrom(InputStream) method", e);
		}

		try {
			result = (M) parseFromInputStream.invoke(null, in);
		} catch (InvocationTargetException e) {
			if (IOException.class.isAssignableFrom(e.getCause().getClass())) {
				throw (IOException) e.getCause();
			}
		} catch (Exception e) {
			throw new HttpMessageNotReadableException(
					"Could not parse HttpInputMessage", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public M fromXml(Readable in) throws IOException {

		M result = null;
		Method newBuilder;
		try {
			newBuilder = messageClass.getMethod("newBuilder");
		} catch (SecurityException e) {
			throw new HttpMessageNotReadableException(
					"Unable to access the newBuilder() method", e);
		} catch (NoSuchMethodException e) {
			throw new HttpMessageNotReadableException(
					"Could not find the newBuilder() method", e);
		}

		try {
			Message.Builder builder = (Message.Builder) newBuilder.invoke(null);
			XmlFormat.merge(in, builder);
			result = (M) builder.build();
		} catch (InvocationTargetException e) {
			if (IOException.class.isAssignableFrom(e.getCause().getClass())) {
				throw (IOException) e.getCause();
			}
		} catch (Exception e) {
			throw new HttpMessageNotReadableException(
					"Could not parse HttpInputMessage", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public M fromJson(Readable in) throws IOException {

		M result = null;
		Method newBuilder;
		try {
			newBuilder = messageClass.getMethod("newBuilder");
		} catch (SecurityException e) {
			throw new HttpMessageNotReadableException(
					"Unable to access the newBuilder() method", e);
		} catch (NoSuchMethodException e) {
			throw new HttpMessageNotReadableException(
					"Could not find the newBuilder() method", e);
		}

		try {
			Message.Builder builder = (Message.Builder) newBuilder.invoke(null);
			JsonFormat.merge(in, builder);
			result = (M) builder.build();
		} catch (InvocationTargetException e) {
			if (IOException.class.isAssignableFrom(e.getCause().getClass())) {
				throw (IOException) e.getCause();
			}
		} catch (Exception e) {
			throw new HttpMessageNotReadableException(
					"Could not parse HttpInputMessage", e);
		}
		return result;
	}

	protected Class<M> getMessageClass() {
		return messageClass;
	}

}
