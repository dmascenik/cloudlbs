package com.cloudlbs.platform.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.BaseSpringContextTestCase;
import com.cloudlbs.platform.domain.App;
import com.cloudlbs.sls.protocol.AuthenticationProto.AuthenticationMessage;
import com.cloudlbs.sls.protocol.AuthenticationProto.AuthenticationMessage.Builder;

public class DeviceAuthenticationServiceTest extends BaseSpringContextTestCase {

	@Autowired
	private GenericService<AuthenticationMessage> deviceAuthenticationService;

	@Autowired
	private GenericService<App> appService;

	private App whereru;

	@Test
	@Transactional
	public void testAuthenticateGood() {
		Builder b = AuthenticationMessage.newBuilder();
		b.setAppGuid(whereru.getGuid());
		b.setUsername("danmascenik");
		b.setPassword("12345");
		b.setDeviceUniqueId("ABCDEF");
		AuthenticationMessage authIn = b.build();

		AuthenticationMessage auth = deviceAuthenticationService
				.createEntity(authIn);
		assertNotNull(auth);
		assertTrue(auth.getSuccess());

		// This one should hit the cache
		AuthenticationMessage cachedAuth = deviceAuthenticationService
				.retrieveEntity(auth.getToken());
		assertNotNull(cachedAuth);
		assertTrue(cachedAuth.getSuccess());
		assertFalse(cachedAuth.getInvalidToken());

		// Logout
		deviceAuthenticationService.deleteEntity(auth.getToken());
		cachedAuth = deviceAuthenticationService
				.retrieveEntity(auth.getToken());
		assertNotNull(cachedAuth);
		assertFalse(cachedAuth.getSuccess());
		assertTrue(cachedAuth.getInvalidToken());
	}

	@Test
	@Transactional
	public void testAuthenticateBad() {
		Builder b = AuthenticationMessage.newBuilder();
		b.setAppGuid(whereru.getGuid());
		b.setUsername("danmascenik");
		b.setPassword("bad");
		b.setDeviceUniqueId("ABCDEF");
		AuthenticationMessage authIn = b.build();

		AuthenticationMessage auth = deviceAuthenticationService
				.createEntity(authIn);
		assertNotNull(auth);
		assertFalse(auth.getSuccess());
		assertFalse(auth.hasToken());
	}

	@Before
	public void before() throws Exception {
		SearchResult<App> results = appService.search(new Query(
				"name: WhereRU\\?", 0, 1));
		whereru = results.getValues().get(0);
	}

}
