package com.cloudlbs.core.utils.remote;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cloudlbs.core.utils.security.HttpRequestSignatureUtils;
import com.cloudlbs.core.utils.security.PublicPrivateKeyUtils;
import com.cloudlbs.core.utils.security.SessionAuthenticationToken;
import com.cloudlbs.core.utils.security.SignatureAuthenticationToken;
import com.google.protobuf.Message;

/**
 * For signature-based authentication, the <code>urlStub</code> must be signed.
 * The <code>urlStub</code> represents the part of the resource URL that comes
 * after the host, port and context path of the application (that you would get
 * from {@link HttpServletRequest#getContextPath()}. For example, you would sign
 * the part in curly braces below:<br/>
 * <br/>
 * <code>http://my.host.com:8080/myApp{<b><i><u>/customer/12345.xml</u></i></b>}</code>
 * <br/>
 * <br/>
 * It is important to take HTTP proxy servers into account. The portion of the
 * URL that maps to the context path on the backend may be longer or shorter
 * than what is exposed by the proxy server, or may not be exposed at all.
 * 
 * @author Dan Mascenik
 * 
 */
public class SecureRestTemplate extends RestTemplate {

	private String baseProtobufPackageName;

	public SecureRestTemplate() throws IllegalAccessException {
		super();
		setDefaultMessageConverters();
	}

	public SecureRestTemplate(ClientHttpRequestFactory requestFactory)
			throws IllegalAccessException {
		super(requestFactory);
		setDefaultMessageConverters();
	}

	public SecureRestTemplate(String baseProtobufPackageName)
			throws IllegalAccessException {
		super();
		this.baseProtobufPackageName = baseProtobufPackageName.trim();
		setDefaultMessageConverters();
	}

	public SecureRestTemplate(ClientHttpRequestFactory requestFactory,
			String baseProtobufPackageName) throws IllegalAccessException {
		super(requestFactory);
		this.baseProtobufPackageName = baseProtobufPackageName.trim();
		setDefaultMessageConverters();
	}

	private void setDefaultMessageConverters() throws IllegalAccessException {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		if (baseProtobufPackageName != null) {
			try {
				messageConverters.add(new ProtobufHttpMessageConverter(
						baseProtobufPackageName));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		setMessageConverters(messageConverters);
	}

	public <T> T getForObject(String sessionId, String url,
			Class<T> responseType, Object... urlVariables)
			throws RestClientException {

		return getResponseBody(url, HttpMethod.GET, null, responseType,
				getSessionHeader(sessionId), urlVariables);
	}

	public <T> T getForObjectPreAuth(String username, String url,
			Class<T> responseType, Object... urlVariables)
			throws RestClientException {

		return getResponseBody(url, HttpMethod.GET, null, responseType,
				getPreAuthHeader(username), urlVariables);
	}

	public <T> T postForObject(String sessionId, String url, Object request,
			Class<T> responseType, Object... urlVariables)
			throws RestClientException {

		return getResponseBody(url, HttpMethod.POST, request, responseType,
				getSessionHeader(sessionId), urlVariables);
	}

	public <T> T postForObjectPreAuth(String username, String url,
			Object request, Class<T> responseType, Object... urlVariables)
			throws RestClientException {
		return getResponseBody(url, HttpMethod.POST, request, responseType,
				getPreAuthHeader(username), urlVariables);
	}

	public <T> T put(String url, Object request, Class<T> responseType,
			Object... urlVariables) throws RestClientException {

		return getResponseBody(url, HttpMethod.PUT, request, responseType,
				null, urlVariables);
	}

	public <T> T put(String sessionId, String url, Object request,
			Class<T> responseType, Object... urlVariables)
			throws RestClientException {

		return getResponseBody(url, HttpMethod.PUT, request, responseType,
				getSessionHeader(sessionId), urlVariables);
	}

	public <T> T putPreAuth(String username, String url, Object request,
			Class<T> responseType, Object... urlVariables)
			throws RestClientException {

		return getResponseBody(url, HttpMethod.PUT, request, responseType,
				getPreAuthHeader(username), urlVariables);
	}

	public <T> void delete(String sessionId, String url, Class<T> responseType,
			Object... urlVariables) throws RestClientException {

		HttpHeaders headers = getSessionHeader(sessionId);
		HttpEntity<T> requestEntity = new HttpEntity<T>(headers);

		exchange(url, HttpMethod.DELETE, requestEntity, responseType,
				urlVariables);
	}

	public <T> void deletePreAuth(String username, String url,
			Class<T> responseType, Object... urlVariables)
			throws RestClientException {

		HttpHeaders headers = getPreAuthHeader(username);
		HttpEntity<T> requestEntity = new HttpEntity<T>(headers);

		exchange(url, HttpMethod.DELETE, requestEntity, responseType,
				urlVariables);
	}

	public <T> T getForObject(String clientId, PrivateKey key, String baseUrl,
			String urlStub, Class<T> responseType, Object... urlVariables)
			throws RestClientException {

		HttpMethod get = HttpMethod.GET;
		String strToSign = HttpRequestSignatureUtils.getStringToSign(get,
				urlStub, (Message) null);
		String signature = PublicPrivateKeyUtils.sign(strToSign, key);

		return getResponseBody(baseUrl + urlStub, get, null, responseType,
				getSignatureHeader(clientId, signature), urlVariables);
	}

	public <T> T postForObject(String clientId, PrivateKey key, String baseUrl,
			String urlStub, Message message, Class<T> responseType,
			Object... urlVariables) throws RestClientException {

		HttpMethod post = HttpMethod.POST;
		String strToSign = HttpRequestSignatureUtils.getStringToSign(post,
				urlStub, message);
		String signature = PublicPrivateKeyUtils.sign(strToSign, key);

		return getResponseBody(baseUrl + urlStub, post, message, responseType,
				getSignatureHeader(clientId, signature), urlVariables);
	}

	public <T> T put(String clientId, PrivateKey key, String baseUrl,
			String urlStub, Message message, Class<T> responseType,
			Object... urlVariables) throws RestClientException {

		HttpMethod put = HttpMethod.PUT;
		String strToSign = HttpRequestSignatureUtils.getStringToSign(put,
				urlStub, message);
		String signature = PublicPrivateKeyUtils.sign(strToSign, key);

		return getResponseBody(baseUrl + urlStub, put, message, responseType,
				getSignatureHeader(clientId, signature), urlVariables);
	}

	public <T> void delete(String clientId, PrivateKey key, String baseUrl,
			String urlStub, Class<T> responseType, Object... urlVariables)
			throws RestClientException {

		HttpMethod delete = HttpMethod.DELETE;
		String strToSign = HttpRequestSignatureUtils.getStringToSign(delete,
				urlStub, (Message) null);
		String signature = PublicPrivateKeyUtils.sign(strToSign, key);

		HttpHeaders headers = getSignatureHeader(clientId, signature);
		HttpEntity<T> requestEntity = new HttpEntity<T>(headers);

		exchange(baseUrl + urlStub, delete, requestEntity, responseType,
				urlVariables);
	}

	@SuppressWarnings("unchecked")
	private <T> T getResponseBody(String url, HttpMethod method,
			Object request, Class<T> responseType, HttpHeaders headers,
			Object... urlVariables) {

		HttpEntity<T> requestEntity;
		if (request != null && headers == null) {
			requestEntity = new HttpEntity<T>((T) request);
		} else if (request == null && headers != null) {
			requestEntity = new HttpEntity<T>(headers);
		} else if (request != null && headers != null) {
			requestEntity = new HttpEntity<T>((T) request, headers);
		} else {
			throw new IllegalArgumentException(
					"HttpHeaders and/or request object must be non-null");
		}

		ResponseEntity<T> response = exchange(url, method, requestEntity,
				responseType, urlVariables);

		return response.getBody();
	}

	private HttpHeaders getSessionHeader(String sessionId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(SessionAuthenticationToken.SESSION_HEADER, sessionId);
		return headers;
	}

	private HttpHeaders getSignatureHeader(String clientId, String signature) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(SignatureAuthenticationToken.CLIENT_ID_HEADER, clientId);
		headers.add(SignatureAuthenticationToken.SIGNATURE_HEADER, signature);
		return headers;
	}

	private HttpHeaders getPreAuthHeader(String username) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-PreAuth-User", username);
		return headers;
	}

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());

}
