package com.cloudlbs.core.utils.web;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import com.cloudlbs.core.utils.protocol.SearchResultMessageConverter;
import com.cloudlbs.core.utils.test.Item;
import com.cloudlbs.core.utils.test.ItemMessageConverter;
import com.cloudlbs.core.utils.test.ItemProto.ItemMessage;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author Dan Mascenik
 * 
 */
// This works in Eclipse, but not in the standard test runner
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContext.xml" })
public class GenericControllerMappingTest extends Assert implements
		ApplicationContextAware {

	private ApplicationContext applicationContext;
	private ItemMessageConverter conv = new ItemMessageConverter();
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HandlerAdapter handlerAdapter;

	@Test
	public void testPostResourceWithProtobufResponse() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item/");
		request.setMethod("POST");
		handle(request, response);
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					response.getContentAsByteArray());
			conv.fromMessage(in);
		} catch (InvalidProtocolBufferException e) {
			fail("Didn't get back a valid protobuf object");
		}
		assertEquals("application/vnd.google.protobuf",
				response.getContentType());
	}

	@Test
	public void testPostResourceWithProtobufResponseNoSlash() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item");
		request.setMethod("POST");
		handle(request, response);
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					response.getContentAsByteArray());
			conv.fromMessage(in);
		} catch (InvalidProtocolBufferException e) {
			fail("Didn't get back a valid protobuf object");
		}
		assertEquals("application/vnd.google.protobuf",
				response.getContentType());
	}

	@Test
	public void testPostResourceWithXmlResponseByPath() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item/xml");
		request.setMethod("POST");
		try {
			handle(request, response);
			fail("POST request shouldn't work here");
		} catch (HttpRequestMethodNotSupportedException e) {
		}
		// handle(request, response);
		// String s = response.getContentAsString();
		// assertTrue("Didn't get the expected XML string",
		// s.startsWith("<FixedLocationMessage>"));
		// assertEquals("text/xml", response.getContentType());
	}

	@Test
	public void testPostResourceWithXmlResponseByExt() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item.xml");
		request.setMethod("POST");
		handle(request, response);
		String s = response.getContentAsString();
		assertTrue("Didn't get the expected XML string",
				s.startsWith("<ItemMessage>"));
		assertEquals("text/xml", response.getContentType());
	}

	@Test
	public void testUnmappedPostResourceWithXmlResponseByExt() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/xxx.xml");
		request.setMethod("POST");
		try {
			handle(request, response);
			fail("Should not have found any handler");
		} catch (AssertionFailedError e) {
		}
	}

	@Test
	public void testPostResourceWithJsonResponse() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item/json");
		request.setMethod("POST");
		try {
			handle(request, response);
			fail("POST request shouldn't work here");
		} catch (HttpRequestMethodNotSupportedException e) {
		}
		// handle(request, response);
		// String s = response.getContentAsString();
		// assertTrue("Didn't get the expected JSON string", s.startsWith("{"));
		// assertEquals("text/json", response.getContentType());
	}

	@Test
	public void testDeleteResourceWithProtobufResponse() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item/abc");
		request.setMethod("DELETE");
		handle(request, response);
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					response.getContentAsByteArray());
			conv.fromMessage(in);
		} catch (InvalidProtocolBufferException e) {
			fail("Didn't get back a valid protobuf object");
		}
		assertEquals("application/vnd.google.protobuf",
				response.getContentType());
	}

	@Test
	public void testDeleteResourceWithXmlResponse() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item/abc.xml");
		request.setMethod("DELETE");
		handle(request, response);
		String s = response.getContentAsString();
		assertTrue("Didn't get the expected XML string",
				s.startsWith("<ItemMessage>"));
		assertEquals("text/xml", response.getContentType());
	}

	@Test
	public void testGetResourceWithProtobufResponse() throws Exception {
		request.setRequestURI("/item/abc");
		request.setMethod("GET");
		handle(request, response);
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					response.getContentAsByteArray());
			conv.fromMessage(in);
		} catch (InvalidProtocolBufferException e) {
			fail("Didn't get back a valid protobuf object");
		}
		assertEquals("application/vnd.google.protobuf",
				response.getContentType());
	}

	@Test
	public void testGetResourceWithXmlResponse() throws Exception {
		request.setRequestURI("/item/abc.xml");
		request.setMethod("GET");
		handle(request, response);
		String s = response.getContentAsString();
		assertTrue("Didn't get the expected XML string",
				s.startsWith("<ItemMessage>"));
		assertEquals("text/xml", response.getContentType());
	}

	@Test
	public void testDeleteBadMapping() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item/def/abc.xml");
		request.setMethod("DELETE");
		try {
			handle(request, response);
			fail("Should not have found any handler");
		} catch (AssertionFailedError e) {
		}
	}

	@Test
	public void testGetQueryWithProtobufResponse() throws Exception {
		request.setRequestURI("/item/query");
		request.setQueryString("q=abc");
		request.setMethod("GET");
		handle(request, response);
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					response.getContentAsByteArray());
			new SearchResultMessageConverter<ItemMessage, Item>(
					ItemMessage.items, conv).fromMessage(in);
		} catch (InvalidProtocolBufferException e) {
			fail("Didn't get back a valid protobuf object");
		}
		assertEquals("application/vnd.google.protobuf",
				response.getContentType());
	}

	@Test
	public void testGetQueryWithXmlResponse() throws Exception {
		request.setRequestURI("/item/query.xml");
		request.setQueryString("q=abc");
		request.setMethod("GET");
		handle(request, response);
		String s = response.getContentAsString();
		assertTrue("Didn't get the expected XML string", s.startsWith("<"));
		assertEquals("text/xml", response.getContentType());
	}

	@Test
	public void testPostQueryWithProtobufResponse() throws Exception {
		request.setContent(getQueryMessage().toByteArray());
		request.setRequestURI("/item/query");
		request.setMethod("POST");
		handle(request, response);
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					response.getContentAsByteArray());
			new SearchResultMessageConverter<ItemMessage, Item>(
					ItemMessage.items, conv).fromMessage(in);
		} catch (InvalidProtocolBufferException e) {
			fail("Didn't get back a valid protobuf object");
		}
		assertEquals("application/vnd.google.protobuf",
				response.getContentType());
	}

	@Test
	public void testPostQueryWithXmlResponse() throws Exception {
		request.setContent(getQueryMessage().toByteArray());
		request.setRequestURI("/item/query.xml");
		request.setMethod("POST");
		handle(request, response);
		String s = response.getContentAsString();
		assertTrue("Didn't get the expected XML string", s.startsWith("<"));
		assertEquals("text/xml", response.getContentType());
	}

	@Test
	public void testPutWithProtobufResponse() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item/query");
		request.setMethod("PUT");
		handle(request, response);
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					response.getContentAsByteArray());
			new SearchResultMessageConverter<ItemMessage, Item>(
					ItemMessage.items, conv).fromMessage(in);
		} catch (InvalidProtocolBufferException e) {
			fail("Didn't get back a valid protobuf object");
		}
		assertEquals("application/vnd.google.protobuf",
				response.getContentType());
	}

	@Test
	public void testPutWithXmlResponse() throws Exception {
		request.setContent(getItemMessage().toByteArray());
		request.setRequestURI("/item/query.xml");
		request.setMethod("PUT");
		handle(request, response);
		String s = response.getContentAsString();
		assertTrue("Didn't get the expected XML string", s.startsWith("<"));
		assertEquals("text/xml", response.getContentType());
	}

	@Before
	public void setUp() throws Exception {
		this.request = new MockHttpServletRequest();
		this.response = new MockHttpServletResponse();
		this.handlerAdapter = applicationContext
				.getBean(AnnotationMethodHandlerAdapter.class);
	}

	void handle(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final HandlerMapping handlerMapping = applicationContext
				.getBean(DefaultAnnotationHandlerMapping.class);
		final HandlerExecutionChain handler = handlerMapping
				.getHandler(request);
		assertNotNull(
				"No handler found for request, check you request mapping",
				handler);

		final Object controller = handler.getHandler();
		final HandlerInterceptor[] interceptors = handlerMapping.getHandler(
				request).getInterceptors();
		for (HandlerInterceptor interceptor : interceptors) {
			final boolean carryOn = interceptor.preHandle(request, response,
					controller);
			if (!carryOn) {
				return;
			}
		}
		handlerAdapter.handle(request, response, controller);
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		applicationContext = ctx;

	}

	public ItemMessage getItemMessage() {
		Item item = new Item("abc");
		item.setReqString("def");
		return conv.toMessage(item);
	}

	public QueryMessage getQueryMessage() {
		QueryMessage.Builder b = QueryMessage.newBuilder();
		return b.build();
	}
}
