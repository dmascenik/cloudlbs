package com.cloudlbs.core.utils.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.protocol.QueryMessageConverter;
import com.cloudlbs.core.utils.protocol.SearchResultMessageConverter;
import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.platform.protocol.SearchProto.SearchResultMessage;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import com.google.protobuf.Message;

/**
 * This generic class provides a simple way of mapping RESTful web operations on
 * Protocol Buffer {@link Message}s to service operations on {@link Entity}
 * types.<br/>
 * <br/>
 * All results are returned in the format indicated by the URL path extension.
 * Valid extensions are <code>xml</code> and <code>json</code>, defaulting to
 * Google Protobuf if no extension is specified. <br/>
 * <br/>
 * For example, <code>GET /.../12345.json</code> refers to resource with
 * id=12345 and will return the resource in JSON format. <br/>
 * <br/>
 * The following request mappings are provided:
 * <table>
 * <tr>
 * <th>Method</th>
 * <th>Path Examples</th>
 * <th>Operation</th>
 * </tr>
 * <tr>
 * <td valign="top" nowrap="true">POST</td>
 * <td valign="top" nowrap="true"><code>/{base}</code><br/>
 * <code>/{base}/</code><br/>
 * <code>/{base}.xml</code><br/>
 * <code>/{base}.json</code></td>
 * <td valign="top" nowrap="true">Create a new resource and return the result in
 * the desired format</td>
 * </tr>
 * <tr>
 * <td valign="top" nowrap="true">GET</td>
 * <td valign="top" nowrap="true"><code>/{base}/abcdef</code><br/>
 * <code>/{base}/abcdef.xml</code><br/>
 * <code>/{base}/abcdef.json</code></td>
 * <td valign="top" nowrap="true">Returns a specified resource</td>
 * </tr>
 * <tr>
 * <td valign="top" nowrap="true">DELETE</td>
 * <td valign="top" nowrap="true"><code>/{base}/abcdef</code><br/>
 * <code>/{base}/abcdef.xml</code><br/>
 * <code>/{base}/abcdef.json</code></td>
 * <td valign="top" nowrap="true">Delete a resource, returning the deleted
 * resource</td>
 * </tr>
 * <tr>
 * <td valign="top" nowrap="true">PUT</td>
 * <td valign="top" nowrap="true"><code>/{base}/abcdef</code><br/>
 * <code>/{base}/abcdef.xml</code><br/>
 * <code>/{base}/abcdef.json</code></td>
 * <td valign="top" nowrap="true">Update a resource, returning the modified
 * resource</td>
 * </tr>
 * <tr>
 * <td valign="top" nowrap="true">GET</td>
 * <td valign="top" nowrap="true"><code>/{base}/query</code><br/>
 * <code>/{base}/query.xml</code><br/>
 * <code>/{base}/query.json</code></td>
 * <td valign="top" nowrap="true">Return all the matching resources matching a
 * Lucene query string provided as the request parameter &quot;q&quot;</td>
 * </tr>
 * <tr>
 * <td valign="top" nowrap="true">POST</td>
 * <td valign="top" nowrap="true"><code>/{base}/query</code><br/>
 * <code>/{base}/query.xml</code><br/>
 * <code>/{base}/query.json</code></td>
 * <td valign="top" nowrap="true">Return all the matching resources given a
 * {@link QueryMessage} protobuf object in the request content</td>
 * </tr>
 * </table>
 * 
 * @author Dan Mascenik
 * 
 * @param <M>
 *            A Protobuf {@link Message} type
 * @param <T>
 *            An {@link Entity} type
 * 
 */
public abstract class GenericController<M extends Message, T> {

	protected MessageConverter<M, T> converter;
	protected MessageConverter<QueryMessage, Query> queryConverter;
	protected MessageConverter<SearchResultMessage, SearchResult<T>> resultsConverter;
	protected GenericService<T> service;

	protected GenericController(GenericService<T> service,
			MessageConverter<M, T> converter,
			GeneratedExtension<SearchResultMessage, List<M>> extension) {
		Assert.notNull(service);
		Assert.notNull(converter);
		this.service = service;
		this.converter = converter;
		this.queryConverter = new QueryMessageConverter();
		this.resultsConverter = new SearchResultMessageConverter<M, T>(
				extension, converter);
	}

	/**
	 * If the caller is authenticated, indicates whether a
	 * {@link PreAuthenticatedAuthenticationToken} is allowed. This would
	 * indicate that we're just trusting that the user is who they say they are.
	 * This may be allowed for a service that is never exposed publicly, but is
	 * called from inside the firewall by another instance that has already
	 * performed the authentication. For any publicly exposed service, this
	 * would be forbidden since it would be a huge security hole.
	 * 
	 */
	protected abstract boolean isPreauthenticationAllowed();

	protected void checkAuthentication(HttpServletResponse response)
			throws IOException {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth != null && !isPreauthenticationAllowed()) {
			if (PreAuthenticatedAuthenticationToken.class.isAssignableFrom(auth
					.getClass())) {
				/*
				 * Pre-authenticated tokens not allowed for this controller
				 */
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				throw new RuntimeException(getClass()
						+ " does not allow pre-authenticated security tokens");
			}
		}
	}

	/**
	 * Creates a new entity from a Google Protobuf message.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "", method = { RequestMethod.POST })
	public void postResource(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		checkAuthentication(response);
		String ext = null;
		String[] uriParts = request.getRequestURI().split("[.]");
		if (uriParts.length > 1) {
			ext = uriParts[uriParts.length - 1];
		}
		M message = recvMessage(request, response);
		T obj = converter.fromMessage(message);
		T entity = service.createEntity(obj);
		M result = converter.toMessage(entity);
		sendMessage(ext, result, converter, request, response);
	}

	/**
	 * Gets the resource residing at this URL.
	 * 
	 * @param stub
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/{stub}", method = { RequestMethod.GET })
	public void getResource(@PathVariable String stub,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		checkAuthentication(response);
		String ext = null;
		String id = stub;
		String uri = request.getRequestURI();
		String[] uriParts = uri.split("[.]");
		if (uriParts.length > 1) {
			ext = uriParts[uriParts.length - 1];
		}
		T entity = service.retrieveEntity(id);
		M message = converter.toMessage(entity);
		sendMessage(ext, message, converter, request, response);
	}

	/**
	 * Deletes the resource residing at this URL.
	 * 
	 * @param stub
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/{stub}", method = { RequestMethod.DELETE })
	public void deleteResource(@PathVariable String stub,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		checkAuthentication(response);
		String ext = null;
		String id = stub;
		String uri = request.getRequestURI();
		String[] uriParts = uri.split("[.]");
		if (uriParts.length > 1) {
			ext = uriParts[uriParts.length - 1];
		}
		T entity = service.retrieveEntity(id);
		service.deleteEntity(id);
		M message = converter.toMessage(entity);
		sendMessage(ext, message, converter, request, response);
	}

	/**
	 * Returns matching entities for the provided {@link QueryMessage}.
	 * 
	 * @param ext
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/query", method = { RequestMethod.POST })
	public void postQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		checkAuthentication(response);

		String ext = null;
		String uri = request.getRequestURI();
		String[] stubParts = uri.split("[.]");
		if (stubParts.length > 1) {
			ext = uri.substring(uri.lastIndexOf(".") + 1);
		}

		QueryMessage queryMessage = recvQuery(request, response);
		log.debug("Received query:\n" + queryMessage.toString());

		Query query = queryConverter.fromMessage(queryMessage);
		SearchResult<T> results = service.search(query);
		SearchResultMessage resultMessage = resultsConverter.toMessage(results);
		sendMessage(ext, resultMessage, resultsConverter, request, response);
	}

	/**
	 * Returns matching entities for the provided query params.<br/>
	 * <br/>
	 * Valid query params are:<br/>
	 * <br/>
	 * <code>q</code> - A Lucene-formatted query string<br/>
	 * <code>startIndex</code> - The index of the first result to return<br/>
	 * <code>count</code> - Total number of results to return<br/>
	 * <code>scope</code> - The base GUID of a scope-tree to limit the search
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/query", method = { RequestMethod.GET })
	public void getQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		checkAuthentication(response);

		String ext = null;
		String uri = request.getRequestURI();
		String[] stubParts = uri.split("[.]");
		if (stubParts.length > 1) {
			ext = uri.substring(uri.lastIndexOf(".") + 1);
		}

		String q = request.getParameter("q");
		if (q == null) {
			q = "";
		}
		int count = 10;
		int startIndex = 0;
		try {
			count = Integer.valueOf(request.getParameter("count"));
		} catch (Exception e) {
		}
		try {
			startIndex = Integer.valueOf(request.getParameter("startIndex"));
		} catch (Exception e) {
		}
		String scopeGuid = request.getParameter("scope");
		Query query = new Query(q, startIndex, count, scopeGuid);
		SearchResult<T> results = service.search(query);
		SearchResultMessage resultMessage = resultsConverter.toMessage(results);
		sendMessage(ext, resultMessage, resultsConverter, request, response);
	}

	/**
	 * Updates the resource residing at the given URL.
	 * 
	 * @param ext
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/{stub}", method = { RequestMethod.PUT })
	public void putResource(@PathVariable String stub,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		checkAuthentication(response);

		String ext = null;
		String id = null;
		String[] stubParts = stub.split("[.]");
		if (stubParts.length == 1) {
			id = stubParts[0];
		}
		if (stubParts.length > 1) {
			id = stub.substring(0, stub.lastIndexOf("."));
			ext = stubParts[stubParts.length - 1];
		}
		M message = recvMessage(request, response);
		T obj = converter.fromMessage(message);
		T entity = service.updateEntity(id, obj,
				converter.getUnsetMessageFields(message));
		M result = converter.toMessage(entity);
		sendMessage(ext, result, converter, request, response);
	}

	protected M recvMessage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream in = request.getInputStream();
		M result = converter.fromMessage(in);
		return result;
	}

	protected QueryMessage recvQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream in = request.getInputStream();
		QueryMessage result = queryConverter.fromMessage(in);
		return result;
	}

	protected <MM extends Message> void sendMessage(String ext, MM message,
			MessageConverter<MM, ?> converter, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if ("json".equals(ext)) {
			sendJson(message, converter, request, response);
		} else if ("xml".equals(ext)) {
			sendXml(message, converter, request, response);
		} else {
			sendPb(message, converter, request, response);
		}
	}

	protected <MM extends Message> void sendJson(MM message,
			MessageConverter<MM, ?> converter, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String json = converter.toJson(message);
		byte[] bytes = json.getBytes();
		String contentType = "text/json";
		response.setContentLength(bytes.length);
		response.setContentType(contentType);
		OutputStream out = response.getOutputStream();
		out.write(bytes);
	}

	protected <MM extends Message> void sendXml(MM message,
			MessageConverter<MM, ?> converter, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String xml = converter.toXml(message);
		byte[] bytes = xml.getBytes();
		String contentType = "text/xml";
		response.setContentLength(bytes.length);
		response.setContentType(contentType);
		OutputStream out = response.getOutputStream();
		out.write(bytes);
	}

	protected <MM extends Message> void sendPb(MM message,
			MessageConverter<MM, ?> converter, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		byte[] bytes = message.toByteArray();
		String contentType = "application/vnd.google.protobuf";

		response.setContentLength(bytes.length);
		response.setContentType(contentType);
		OutputStream out = response.getOutputStream();
		out.write(bytes);
	}

	private Logger log = LoggerFactory.getLogger(getClass());

}
