package com.cloudlbs.core.utils.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.cloudlbs.core.utils.protocol.ProtobufMessageConverter;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.google.protobuf.Message;

/**
 * @author Dan Mascenik
 * 
 */
@RunWith(JMock.class)
public abstract class JMockWebControllerTestCase<M extends Message, T> extends
		Assert {

	protected Mockery context = new JUnit4Mockery();

	protected ProtobufMessageConverter<M, T> converter;

	protected HttpServletRequest request = context.mock(
			HttpServletRequest.class, "request");
	protected HttpServletResponse response = context.mock(
			HttpServletResponse.class, "response");

	protected GenericService<T> service;

	protected GenericController<M, T> controller;

	/**
	 * Set up the converter, service and controller in this method
	 */
	protected abstract void before() throws Exception;

	@Before
	public void validate() throws Exception {
		before();
		assertNotNull("protected field \"converter\" "
				+ "must be initialized in a before() method", converter);
		assertNotNull("protected field \"service\" "
				+ "must be initialized in a before() method", service);
		assertNotNull("protected field \"controller\" "
				+ "must be initialized in a before() method", controller);
	}

	protected ServletInputStream getServletInputStream(T entity) {
		M message = converter.toMessage(entity);
		final ByteArrayInputStream is = new ByteArrayInputStream(
				message.toByteArray());
		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return is.read();
			}
		};
	}

	protected ByteArrayServletOutputStream getServletOutputStream() {
		return new ByteArrayServletOutputStream();
	}

	protected class ByteArrayServletOutputStream extends ServletOutputStream {

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		@Override
		public void write(int b) throws IOException {
			os.write(b);
		}

		public byte[] toByteArray() {
			return os.toByteArray();
		}
	}

	protected ServletInputStream getQueryServletInputStream() {

		QueryMessage.Builder b = QueryMessage.newBuilder();
		b.setQ("");
		QueryMessage message = b.build();

		final ByteArrayInputStream is = new ByteArrayInputStream(
				message.toByteArray());
		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return is.read();
			}
		};
	}

}
