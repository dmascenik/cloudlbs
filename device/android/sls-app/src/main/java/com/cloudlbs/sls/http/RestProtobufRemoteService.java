package com.cloudlbs.sls.http;

import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import com.cloudlbs.sls.utils.Logger;
import com.google.protobuf.Message;

/**
 * @author Dan Mascenik
 * 
 */
public abstract class RestProtobufRemoteService<M extends Message> implements
		RemoteService<M> {

	protected HttpClient httpClient;
	protected Class<M> messageClass;
	private String baseUrl;
	private String resourceUrlStub;

	protected boolean useFallbackUrl = false;

	public RestProtobufRemoteService(HttpClient httpClient,
			Class<M> messageClass, String baseUrl, String resourceUrlStub) {
		this.httpClient = httpClient;
		this.messageClass = messageClass;
		this.baseUrl = baseUrl;
		this.resourceUrlStub = resourceUrlStub;
	}

	public String getServiceUri() {
		return baseUrl + resourceUrlStub;
	}

	@Override
	public M get(String guid) throws Exception {
		HttpResponse response = executeRequest("GET", guid, null);
		M message = extractResponse(response);
		return message;
	}

	@Override
	public M create(M representation) throws Exception {
		HttpResponse response = executeRequest("POST", null, representation);
		M message = extractResponse(response);
		return message;
	}

	@Override
	public void delete(String guid) throws Exception {
		executeRequest("DELETE", guid, null);
	}

	@Override
	public M update(String guid, M representation) throws Exception {
		HttpResponse response = executeRequest("PUT", guid, representation);
		M message = extractResponse(response);
		return message;
	}

	private HttpResponse executeRequest(String method, String guid, M entity)
			throws Exception {
		HttpResponse response = null;
		HttpUriRequest request = constructRequest(method, guid, entity);
		try {
			response = httpClient.execute(request);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw e;
		}

		if (response == null) {
			// must have done the fallback logic
			request = constructRequest(method, guid, entity);
			response = httpClient.execute(request);
		}

		response.addHeader("X-Request-URI", request.getURI().toString());
		return response;
	}

	private HttpUriRequest constructRequest(String method, String guid, M entity) {
		HttpUriRequest request;
		if ("GET".equals(method)) {
			request = new HttpGet(getServiceUri() + "/" + guid);
		} else if ("POST".equals(method)) {
			request = new HttpPost(getServiceUri());
			((HttpPost) request).setEntity(new ByteArrayEntity(entity
					.toByteArray()));
		} else if ("DELETE".equals(method)) {
			request = new HttpDelete(getServiceUri() + "/" + guid);
		} else if ("PUT".equals(method)) {
			request = new HttpPut(getServiceUri() + "/" + guid);
			((HttpPut) request).setEntity(new ByteArrayEntity(entity
					.toByteArray()));
		} else {
			throw new IllegalArgumentException("Unhandled HTTP method: "
					+ method);
		}
		return request;
	}

	private M extractResponse(HttpResponse response) throws Exception {
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new RuntimeException("Call returned status " + statusCode
					+ " (" + response.getHeaders("X-Request-URI")[0].getValue()
					+ ")");
		}
		int len = (int) response.getEntity().getContentLength();
		byte[] resp = new byte[len];
		response.getEntity().getContent().read(resp);
		return toMessage(resp);
	}

	@SuppressWarnings("unchecked")
	protected M toMessage(byte[] bytes) throws Exception {
		Method parser = messageClass.getMethod("parseFrom", byte[].class);
		M message = (M) parser.invoke(null, bytes);
		return message;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setResourceUrlStub(String resourceUrlStub) {
		this.resourceUrlStub = resourceUrlStub;
	}

}
