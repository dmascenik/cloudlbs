package com.cloudlbs.core.utils.security;

import java.security.PrivateKey;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import com.google.protobuf.Message;

/**
 * For signature-based authentication, there are different parts of the request
 * that can be signed.<br/>
 * <br/>
 * This class provides centralized business logic in a shared location for
 * extracting the string to be signed from an HTTP request so it can be used by
 * both the caller and the authentication provider.<br/>
 * <br/>
 * Third-party callers of CloudLBS services will need to implement this logic
 * themselves to ensure that their service requests can be authenticated by
 * CloudLBS.
 * 
 * @author Dan Mascenik
 * 
 */
public class HttpRequestSignatureUtils {

	/**
	 * For signature-based authentication, the <code>urlStub</code> must be
	 * signed. The <code>urlStub</code> represents the part of the resource URL
	 * that comes after the host, port and context path of the application (that
	 * you would get from {@link HttpServletRequest#getContextPath()}. For
	 * example, you would sign the part in curly braces below:<br/>
	 * <br/>
	 * <code>http://my.host.com:8080/myApp{<b><i><u>/customer/12345.xml</u></i></b>}</code>
	 * <br/>
	 * <br/>
	 * It is important to take HTTP proxy servers into account. The portion of
	 * the URL that maps to the context path on the backend may be longer or
	 * shorter than what is exposed by the proxy server, or may not be exposed
	 * at all.<br/>
	 * <br/>
	 * 
	 * @see SecureRestTemplate
	 * 
	 * @param <M>
	 * @param method
	 * @param urlStub
	 *            may be null if the method is POST or PUT
	 * @param requestEntity
	 *            {@link HttpEntity#getBody()} must return a {@link Message}.
	 *            This parameter may be null if the method is GET or DELETE
	 * @return the String to sign with a {@link PrivateKey}
	 */
	public static <M extends Message> String getStringToSign(HttpMethod method,
			String urlStub, HttpEntity<M> requestEntity) {
		return urlStub.trim();
	}

	public static <M extends Message> String getStringToSign(HttpMethod method,
			String urlStub, Message message) {
		return urlStub.trim();
	}

	public static <M extends Message> String getStringToSign(
			HttpServletRequest request) {

		String contextPath = request.getContextPath();
		String url = request.getRequestURI();
		return url.substring(contextPath.length()).trim();
	}

}
