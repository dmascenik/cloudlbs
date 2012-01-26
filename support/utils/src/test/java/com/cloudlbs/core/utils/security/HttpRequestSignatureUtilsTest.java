package com.cloudlbs.core.utils.security;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import com.cloudlbs.core.utils.test.ItemProto.ItemMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class HttpRequestSignatureUtilsTest extends Assert {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGetAndDeleteUrl() {
		String urlStub = "  /this.is/a?test=1234 ";
		String result = HttpRequestSignatureUtils.getStringToSign(
				HttpMethod.GET, urlStub, (HttpEntity) null);
		assertEquals(urlStub.trim(), result);
		result = HttpRequestSignatureUtils.getStringToSign(HttpMethod.DELETE,
				urlStub, (HttpEntity) null);
		assertEquals(urlStub.trim(), result);
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testGetAndDeleteEmptyUrlStub() {
		String urlStub = "";
		String result = HttpRequestSignatureUtils.getStringToSign(
				HttpMethod.GET, urlStub, (HttpEntity) null);
		assertEquals(urlStub.trim(), result);
		result = HttpRequestSignatureUtils.getStringToSign(HttpMethod.DELETE,
				urlStub, (HttpEntity) null);
		assertEquals(urlStub.trim(), result);
	}

//	@Test
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public void testUnsupportedMethod() {
//		try {
//			HttpRequestSignatureUtils.getStringToSign(HttpMethod.HEAD, null,
//					(HttpEntity) null);
//			fail("unsupported method should not work");
//		} catch (IllegalArgumentException e) {
//		}
//	}

	@Test
	public void testGetAndDeleteHttpRequest() {
		String urlStub = "/this.is/a?test=1234";

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");
		request.setContextPath("/myApp");
		request.setRequestURI("/myApp" + urlStub);

		assertEquals(urlStub,
				HttpRequestSignatureUtils.getStringToSign(request));

		request.setMethod("DELETE");
		assertEquals(urlStub,
				HttpRequestSignatureUtils.getStringToSign(request));
	}

	@Test
	public void testGetAndDeleteHttpRequestEmptyUrlStub() {
		String urlStub = "";

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");
		request.setContextPath("/myApp");
		request.setRequestURI("/myApp" + urlStub);

		assertEquals(urlStub,
				HttpRequestSignatureUtils.getStringToSign(request));

		request.setMethod("DELETE");
		assertEquals(urlStub,
				HttpRequestSignatureUtils.getStringToSign(request));
	}

//	@Test
//	public void testPostAndPutHttpEntity() {
//		HttpEntity<ItemMessage> he = new HttpEntity<ItemProto.ItemMessage>(
//				getItemMessage());
//		String s1 = HttpRequestSignatureUtils.getStringToSign(HttpMethod.POST,
//				null, he);
//		assertNotNull(s1);
//
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		request.setMethod("POST");
//		request.setContent(getItemMessage().toByteArray());
//		request.setContextPath("/myApp");
//		request.setRequestURI("/myApp");
//
//		assertEquals(s1, HttpRequestSignatureUtils.getStringToSign(request));
//	}
//
//	@Test
//	public void testPostAndPutBigHttpEntity() {
//		HttpEntity<ItemMessage> he = new HttpEntity<ItemProto.ItemMessage>(
//				getBigItemMessage());
//		String s1 = HttpRequestSignatureUtils.getStringToSign(HttpMethod.POST,
//				null, he);
//		assertNotNull(s1);
//
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		request.setMethod("POST");
//		request.setContent(getBigItemMessage().toByteArray());
//		request.setContextPath("/myApp");
//		request.setRequestURI("/myApp");
//
//		assertEquals(s1, HttpRequestSignatureUtils.getStringToSign(request));
//	}

	@Test
	public void testReReadEntityFromStream() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setContent(getItemMessage().toByteArray());
		request.setContextPath("/myApp");
		request.setRequestURI("/myApp");

		HttpRequestSignatureUtils.getStringToSign(request);

		InputStream in = request.getInputStream();
		ItemMessage item = ItemMessage.parseFrom(in);

		assertNotNull(item);
	}

	private ItemMessage getItemMessage() {
		ItemMessage.Builder b = ItemMessage.newBuilder();
		b.setReqString("this is some kind of string");
		return b.build();
	}

//	private ItemMessage getBigItemMessage() {
//		ItemMessage.Builder b = ItemMessage.newBuilder();
//		b.setReqString("this is some kind of string this is some "
//				+ "kind of string this is some kind of string this"
//				+ " is some kind of string this is some kind of "
//				+ "string this is some kind of string this is "
//				+ "some kind of string this is some kind of string "
//				+ "this is some "
//				+ "kind of string this is some kind of string this"
//				+ " is some kind of string this is some kind of "
//				+ "string this is some kind of string this is "
//				+ "some kind of string");
//		return b.build();
//	}
}
