package com.cloudlbs.core.utils.remote;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.cloudlbs.core.utils.ReflectionUtils;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;

public class ProtobufHttpMessageConverter implements
		HttpMessageConverter<Message> {

	private final static MediaType PROTOBUF_MEDIA_TYPE = new MediaType(
			"application", "vnd.google.protobuf");

	private ExtensionRegistry extensionRegistry = ExtensionRegistry
			.newInstance();

	private List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();

	/**
	 * Provides Protobuf HTTP message conversion including conversion for an
	 * explicit list of extensions. This would be instantiated in a Spring bean
	 * configuration as follows (extra white space is not handled well):
	 * 
	 * <pre>
	 * &lt;bean class=" com.cloudlbs.core.utils.remote.ProtobufHttpMessageConverter"&gt;
	 *    &lt;constructor-arg&gt; 
	 *       &lt;list&gt;
	 *          &lt;value&gt;#{T(com.cloudlbs.platform.protocol.UserAccountProto$UserAccountMessage).items}&lt;/value&gt;
	 *       &lt;/list&gt;
	 *    &lt;/constructor-arg&gt; 
	 * &lt;/bean&gt;
	 * </pre>
	 * 
	 * @param extensions
	 */
	public ProtobufHttpMessageConverter(
			GeneratedMessage.GeneratedExtension<?, ?>... extensions) {
		this();
		int i = 0;
		for (GeneratedMessage.GeneratedExtension<?, ?> extension : extensions) {
			extensionRegistry.add(extension);
			i++;
		}
		if (i == 0) {
			log.warn("No Protobuf extensions registered! "
					+ "HTTP remote search will not work!");
		}
	}

	private ProtobufHttpMessageConverter() {
		log.debug("Providing support for: " + PROTOBUF_MEDIA_TYPE);
		supportedMediaTypes.add(PROTOBUF_MEDIA_TYPE);
	}

	/**
	 * Finds extensions by scanning for {@link Message} classes in a named Java
	 * package and registers all message fields that have type
	 * {@link GeneratedMessage.GeneratedExtension}.
	 * 
	 * @param packageName
	 */
	public ProtobufHttpMessageConverter(String packageName)
			throws ClassNotFoundException, IOException, IllegalAccessException {
		this();
		List<Class<?>> classes = ReflectionUtils
				.getClassesForPackage(packageName);
		int i = 0;
		for (Class<?> c : classes) {
			if (Message.class.isAssignableFrom(c)) {
				@SuppressWarnings("unchecked")
				List<GeneratedMessage.GeneratedExtension<?, ?>> extensions = ReflectionUtils
						.getProtobufExtensions((Class<Message>) c);
				for (GeneratedMessage.GeneratedExtension<?, ?> ext : extensions) {
					log.debug("Adding discovered protobuf extension: "
							+ ext.getDescriptor().getFullName());
					extensionRegistry.add(ext);
					i++;
				}
			}
		}
		if (i == 0) {
			log.warn("No Protobuf extensions discovered in package "
					+ packageName + ". HTTP remote search will not work!");
		}
	}

	public List<MediaType> getSupportedMediaTypes() {
		return supportedMediaTypes;
	}

	public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
		this.supportedMediaTypes = supportedMediaTypes;
	}

	@Override
	public boolean canRead(Class<?> messageClass, MediaType mediaType) {
		boolean result = mediaType == null ? false : mediaType
				.isCompatibleWith(PROTOBUF_MEDIA_TYPE);
		log.debug("canRead = " + result + " for " + messageClass.getName()
				+ " with mediaType " + mediaType);
		return result;
	}

	@Override
	public boolean canWrite(Class<?> messageClass, MediaType mediaType) {
		boolean result = Message.class.isAssignableFrom(messageClass)
				&& (mediaType == null ? true : mediaType
						.isCompatibleWith(PROTOBUF_MEDIA_TYPE));
		log.debug("canWrite = " + result + " for " + messageClass.getName()
				+ " with mediaType " + mediaType);
		return result;
	}

	@Override
	public Message read(Class<? extends Message> targetType,
			HttpInputMessage source) throws IOException,
			HttpMessageNotReadableException {

		log.debug("Reading " + targetType.getClass().getName()
				+ " from HttpInputMessage source");

		Message result = null;
		Method parseFromInputStream;
		try {
			parseFromInputStream = targetType.getMethod("parseFrom",
					InputStream.class, ExtensionRegistryLite.class);
		} catch (SecurityException e) {
			throw new HttpMessageNotReadableException(
					"Unable to access the parseFrom(InputStream) method", e);
		} catch (NoSuchMethodException e) {
			throw new HttpMessageNotReadableException(
					"Could not find the parseFrom(InputStream) method", e);
		}

		try {
			result = (Message) parseFromInputStream.invoke(null,
					source.getBody(), extensionRegistry);
		} catch (InvocationTargetException e) {
			if (IOException.class.isAssignableFrom(e.getCause().getClass())) {
				throw (IOException) e.getCause();
			}
		} catch (Exception e) {
			throw new HttpMessageNotReadableException(
					"Could not parse HttpInputMessage", e);
		}

		log.debug("Read " + targetType.getClass().getName() + ".");

		return result;
	}

	@Override
	public void write(Message source, MediaType mediaType,
			HttpOutputMessage target) throws IOException,
			HttpMessageNotWritableException {

		log.debug("Writing " + source.getClass().getName()
				+ " to HttpOutputMessage target");

		if (mediaType == null) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("proto", source.getClass().getCanonicalName());
			mediaType = new MediaType(PROTOBUF_MEDIA_TYPE, params);
		}
		target.getHeaders().setContentType(mediaType);
		target.getBody().write(source.toByteArray());

		log.debug("Wrote " + source.getClass().getName() + ".");
	}

	private Logger log = LoggerFactory
			.getLogger(ProtobufHttpMessageConverter.class);
}
