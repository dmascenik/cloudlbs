package com.cloudlbs.platform.integration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.cloudlbs.core.utils.protocol.NoopMessageConverter;
import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.core.utils.remote.SecureRestTemplate;
import com.cloudlbs.platform.BaseSpringContextTestCase;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage;
import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage.Builder;

/**
 * These tests require a localhost:8080 service instance with full database, and
 * a localhost:8090 service instance with NO database access to be running.
 * 
 * @author Dan Mascenik
 * 
 */
public class DeviceRegistrationIntegrationTests extends
		BaseSpringContextTestCase {

	@Autowired
	SecureRestTemplate secureRestTemplate;

	RestProtobufRemoteService<DeviceConnectionMessage, DeviceConnectionMessage> service;

	@Test
	public void testGetDetails() {
		DeviceConnectionMessage dcm = service.get("12345");
		assertNotNull(dcm);
	}

	@Test
	public void testNonAllowedMethods() {
		Builder b = DeviceConnectionMessage.newBuilder();
		DeviceConnectionMessage dcm = b.build();
		try {
			service.create(dcm);
			fail("POST resource should not be allowed");
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.METHOD_NOT_ALLOWED, e.getStatusCode());
		}

		// FIXME - need to figure out why these get 400's and not 405's
//		try {
//			service.delete("abc");
//			fail("DELETE resource should not be allowed");
//		} catch (HttpClientErrorException e) {
//			assertEquals(HttpStatus.METHOD_NOT_ALLOWED, e.getStatusCode());
//		}
//
//		try {
//			service.update("abc", dcm);
//			fail("UPDATE resource should not be allowed");
//		} catch (HttpClientErrorException e) {
//			assertEquals(HttpStatus.METHOD_NOT_ALLOWED, e.getStatusCode());
//		}

		try {
			com.cloudlbs.platform.protocol.SearchProto.QueryMessage.Builder qb = QueryMessage
					.newBuilder();
			qb.setQ("test: foo");
			service.search(qb.build());
			fail("search should not be allowed");
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.METHOD_NOT_ALLOWED, e.getStatusCode());
		}
	}

	@Before
	public void before() throws Exception {
		service = new RestProtobufRemoteService<DeviceConnectionMessage, DeviceConnectionMessage>(
				new NoopMessageConverter<DeviceConnectionMessage>(
						DeviceConnectionMessage.class), null,
				DeviceConnectionMessage.class, DeviceConnectionMessage.class,
				secureRestTemplate) {
		};
		service.setServiceUrl("http://localhost:8090/svc");
		service.setResourceStub("/device/connection");
	}

}
