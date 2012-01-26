package com.cloudlbs.core.utils.remote;

import java.security.PrivateKey;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.protocol.NoopMessageConverter;
import com.cloudlbs.core.utils.protocol.SearchResultMessageConverter;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.security.HttpRequestSignatureUtils;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.platform.protocol.SearchProto.SearchResultMessage;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import com.google.protobuf.Message;

/**
 * Uses the Spring {@link RestTemplate} to access a web service that uses
 * Protobuf messages. The returned message will be converted into another type
 * if 1) the <b>&lt;T&gt;</b> parameter is different from <b>&lt;M&gt;</b>, and
 * a {@link MessageConverter} has been provided. Otherwise, an internal "no-op"
 * message converter will be applied, and the objects returned from these
 * service methods will be the Protobuf message objects themselves.<br/>
 * <br/>
 * This class relies on an autowired instance of the {@link SecureRestTemplate}. <br/>
 * <br/>
 * If you will be using signature-based authentication, the proper specification
 * of the {@link #serviceUrl} and the {@link #resourceStub} is important. The
 * {@link #serviceUrl} must contain the context path, if any. For more
 * information, see {@link HttpRequestSignatureUtils}.
 * 
 * @see HttpRequestSignatureUtils
 * 
 * @author Dan Mascenik
 * 
 */
public abstract class RestProtobufRemoteService<M extends Message, T> extends
		AbstractRemoteService<M, T> implements RemoteService<M, T> {

	@Autowired
	protected SecureRestTemplate secureRestTemplate;

	private MessageConverter<SearchResultMessage, SearchResult<T>> resultsConverter;
	private MessageConverter<M, T> messageConverter;
	private MessageConverter<SearchResultMessage, SearchResult<M>> noopResultsConverter;

	private String serviceUrl;
	private String resourceStub;
	private boolean noConversion = false;

	/**
	 * @param messageConverter
	 *            May be null if the &lt;M&gt; and &lt;T&gt; parameters are the
	 *            same.
	 * @param extension
	 *            s
	 */
	public RestProtobufRemoteService(MessageConverter<M, T> messageConverter,
			GeneratedExtension<SearchResultMessage, List<M>> extension) {
		super();
		init(messageConverter, extension);
	}

	public RestProtobufRemoteService(MessageConverter<M, T> messageConverter,
			GeneratedExtension<SearchResultMessage, List<M>> extension,
			Class<M> messageClass, Class<T> typeClass) {
		super(messageClass, typeClass);
		init(messageConverter, extension);
	}

	public RestProtobufRemoteService(MessageConverter<M, T> messageConverter,
			GeneratedExtension<SearchResultMessage, List<M>> extension,
			Class<M> messageClass, Class<T> typeClass,
			SecureRestTemplate secureRestTemplate) {
		super(messageClass, typeClass);
		init(messageConverter, extension);
		this.secureRestTemplate = secureRestTemplate;
	}

	public void setRestTemplate(SecureRestTemplate secureRestTemplate) {
		this.secureRestTemplate = secureRestTemplate;
	}

	private void init(MessageConverter<M, T> messageConverter,
			GeneratedExtension<SearchResultMessage, List<M>> extension) {
		this.messageConverter = messageConverter;
		if (getMessageClass() == getTypeClass()) {
			/*
			 * This is the no-op conversion case. Any provided MessageConverter
			 * will be ignored
			 */
			log.debug("M and T parameters are the same. Using no-op "
					+ "message conversion for class " + getClass().getName());
			this.noopResultsConverter = new SearchResultMessageConverter<M, M>(
					extension, new NoopMessageConverter<M>(getMessageClass()));
			noConversion = true;
			if (messageConverter != null) {
				log.warn("Non-null message converter provided "
						+ "to constructor will be ignored for class "
						+ getClass().getName());
			}
		} else {
			if (messageConverter == null) {
				throw new IllegalArgumentException(getClass().getName()
						+ ": A MessageConverter must be "
						+ "provided when the M and T parameters are different");
			}
			this.resultsConverter = new SearchResultMessageConverter<M, T>(
					extension, messageConverter);
		}
	}

	@Override
	public T create(T obj) {
		M message = toMessage(obj);
		return createFromMessage(message);
	}

	@Override
	public T create(String sessionId, T obj) {
		M message = toMessage(obj);
		return createFromMessage(sessionId, message);
	}

	@Override
	public T createPreAuthenticated(String username, T obj) {
		M message = toMessage(obj);
		return createFromMessagePreAuthenticated(username, message);
	}

	@Override
	public T create(String clientId, PrivateKey key, T obj) {
		M message = toMessage(obj);
		return createFromMessage(clientId, key, message);
	}

	@Override
	public T createFromMessage(M message) {
		M result = secureRestTemplate.postForObject(getServiceURL(), message,
				getMessageClass());
		return toType(result);
	}

	@Override
	public T createFromMessage(String sessionId, M message) {
		M result = secureRestTemplate.postForObject(sessionId, getServiceURL(),
				message, getMessageClass());
		return toType(result);
	}

	@Override
	public T createFromMessagePreAuthenticated(String username, M message) {
		M result = secureRestTemplate.postForObjectPreAuth(username,
				getServiceURL(), message, getMessageClass());
		return toType(result);
	}

	@Override
	public T createFromMessage(String clientId, PrivateKey key, M message) {
		M result = secureRestTemplate.postForObject(clientId, key, serviceUrl,
				resourceStub, message, getMessageClass());
		return toType(result);
	}

	@Override
	public T get(String guid) {
		M result = secureRestTemplate.getForObject(
				getServiceURL() + "/" + guid, getMessageClass());
		return toType(result);
	}

	@Override
	public T get(String sessionId, String guid) {
		M result = secureRestTemplate.getForObject(sessionId, getServiceURL()
				+ "/" + guid, getMessageClass());
		return toType(result);
	}

	@Override
	public T getPreAuthenticated(String username, String guid) {
		M result = secureRestTemplate.getForObjectPreAuth(username,
				getServiceURL() + "/" + guid, getMessageClass());
		return toType(result);
	}

	@Override
	public T get(String clientId, PrivateKey key, String guid) {
		M result = secureRestTemplate.getForObject(clientId, key, serviceUrl,
				resourceStub + "/" + guid, getMessageClass());
		return toType(result);
	}

	@Override
	public T update(String guid, T obj) {
		M message = toMessage(obj);
		return updateFromMessage(guid, message);
	}

	@Override
	public T update(String sessionId, String guid, T obj) {
		M message = toMessage(obj);
		return updateFromMessage(sessionId, guid, message);
	}

	@Override
	public T updatePreAuthenticated(String username, String guid, T obj) {
		M message = toMessage(obj);
		return updateFromMessagePreAuthenticated(username, guid, message);
	}

	@Override
	public T update(String clientId, PrivateKey key, String guid, T obj) {
		M message = toMessage(obj);
		return updateFromMessage(clientId, key, guid, message);
	}

	@Override
	public T updateFromMessage(String guid, M obj) {
		M result = secureRestTemplate.put(getServiceURL() + "/" + guid, obj,
				getMessageClass());
		return toType(result);
	}

	@Override
	public T updateFromMessage(String sessionId, String guid, M obj) {
		M result = secureRestTemplate.put(sessionId, getServiceURL() + "/"
				+ guid, obj, getMessageClass());
		return toType(result);
	}

	@Override
	public T updateFromMessagePreAuthenticated(String username, String guid,
			M obj) {
		M result = secureRestTemplate.putPreAuth(username, getServiceURL()
				+ "/" + guid, obj, getMessageClass());
		return toType(result);
	}

	@Override
	public T updateFromMessage(String clientId, PrivateKey key, String guid,
			M obj) {

		M result = secureRestTemplate.put(clientId, key, serviceUrl,
				resourceStub + "/" + guid, obj, getMessageClass());
		return toType(result);
	}

	@Override
	public void delete(String guid) {
		secureRestTemplate.delete(getServiceURL() + "/" + guid);
	}

	@Override
	public void delete(String sessionId, String guid) {
		secureRestTemplate.delete(sessionId, getServiceURL() + "/" + guid,
				getTypeClass());
	}

	@Override
	public void deletePreAuthenticated(String username, String guid) {
		secureRestTemplate.deletePreAuth(username,
				getServiceURL() + "/" + guid, getTypeClass());
	}

	@Override
	public void delete(String clientId, PrivateKey key, String guid) {
		secureRestTemplate.delete(clientId, key, serviceUrl, resourceStub + "/"
				+ guid, getTypeClass());
	}

	@Override
	public SearchResult<T> search(QueryMessage query) {
		SearchResultMessage resultsMessage = secureRestTemplate.postForObject(
				getServiceURL() + "/query", query, SearchResultMessage.class);
		return toSearchResultType(resultsMessage);
	}

	@Override
	public SearchResult<T> search(String sessionId, QueryMessage query) {
		SearchResultMessage resultsMessage = secureRestTemplate.postForObject(
				sessionId, getServiceURL() + "/query", query,
				SearchResultMessage.class);
		return toSearchResultType(resultsMessage);
	}

	@Override
	public SearchResult<T> searchPreAuthenticated(String username,
			QueryMessage query) {
		SearchResultMessage resultsMessage = secureRestTemplate
				.postForObjectPreAuth(username, getServiceURL() + "/query",
						query, SearchResultMessage.class);
		return toSearchResultType(resultsMessage);
	}

	@Override
	public SearchResult<T> search(String clientId, PrivateKey key,
			QueryMessage query) {
		SearchResultMessage resultsMessage = secureRestTemplate.postForObject(
				clientId, key, serviceUrl, resourceStub + "/query", query,
				SearchResultMessage.class);
		return toSearchResultType(resultsMessage);
	}

	public String getServiceURL() {
		return serviceUrl + resourceStub;
	}

	@SuppressWarnings("unchecked")
	private T toType(M message) {
		T obj;
		if (noConversion) {
			obj = (T) message;
		} else {
			obj = messageConverter.fromMessage(message);
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	private M toMessage(T type) {
		M message;
		if (noConversion) {
			message = (M) type;
		} else {
			message = messageConverter.toMessage(type);
		}
		return message;
	}

	@SuppressWarnings("unchecked")
	private SearchResult<T> toSearchResultType(SearchResultMessage message) {
		SearchResult<T> results;
		if (noConversion) {
			results = (SearchResult<T>) noopResultsConverter
					.fromMessage(message);
		} else {
			results = resultsConverter.fromMessage(message);
		}
		return results;
	}

	private Logger log = LoggerFactory.getLogger(getClass());

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public void setResourceStub(String resourceStub) {
		this.resourceStub = resourceStub;
	}
}
