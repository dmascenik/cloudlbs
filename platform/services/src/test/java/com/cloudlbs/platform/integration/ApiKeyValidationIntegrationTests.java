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
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage.Builder;

/**
 * These tests require a localhost:8080 service instance with full database and
 * NO XMPP, and a localhost:8090 service instance with XMPP access and NO
 * database access to be running.
 * 
 * @author Dan Mascenik
 * 
 */
public class ApiKeyValidationIntegrationTests extends BaseSpringContextTestCase {

	@Autowired
	SecureRestTemplate secureRestTemplate;

	RestProtobufRemoteService<AppDetailsMessage, AppDetailsMessage> service;

	@Test
	public void testGetDetails() {
		Builder b = AppDetailsMessage.newBuilder();
		b.setApiKey("PreSDlnGVul0jTfohXgKK4gg");
		b.setAppIdentifier("com.cloudlbs.whereru");
		b.setCertificateFingerprint("BEBC47ADC5A534D4FA15D83F01E3E6CE");
		b.setDeviceUniqueId("12345");

		AppDetailsMessage adm = service.create(b.build());
		assertNotNull(adm);
		assertTrue(adm.getIsValid());
	}

	@Test
	public void testNonAllowedMethods() {
//		Builder b = AppDetailsMessage.newBuilder();
//		b.setApiKey("PreSDlnGVul0jTfohXgKK4gg");
//		AppDetailsMessage adm = b.build();

		// FIXME need to determine why these throw 400's and not 405's
		// try {
		// service.get("abc");
		// fail("GET resource should not be allowed");
		// } catch (HttpClientErrorException e) {
		// assertEquals(HttpStatus.METHOD_NOT_ALLOWED, e.getStatusCode());
		// }
		//
		// try {
		// service.delete("abc");
		// fail("DELETE resource should not be allowed");
		// } catch (HttpClientErrorException e) {
		// assertEquals(HttpStatus.METHOD_NOT_ALLOWED, e.getStatusCode());
		// }
		//
		// try {
		// service.update("abc", adm);
		// fail("UPDATE resource should not be allowed");
		// } catch (HttpClientErrorException e) {
		// assertEquals(HttpStatus.METHOD_NOT_ALLOWED, e.getStatusCode());
		// }

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
		service = new RestProtobufRemoteService<AppDetailsMessage, AppDetailsMessage>(
				new NoopMessageConverter<AppDetailsMessage>(
						AppDetailsMessage.class), null,
				AppDetailsMessage.class, AppDetailsMessage.class,
				secureRestTemplate) {
		};
		service.setServiceUrl("http://localhost:8090/svc");
		service.setResourceStub("/device/app");
	}

}
