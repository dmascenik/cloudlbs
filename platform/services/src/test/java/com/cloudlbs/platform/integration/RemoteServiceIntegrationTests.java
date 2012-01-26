package com.cloudlbs.platform.integration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.platform.BaseSpringContextTestCase;
import com.cloudlbs.platform.core.RemoteableService;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage;
import com.cloudlbs.platform.service.internal.SystemPropertyService;

/**
 * These tests requires a localhost:8080 service instance with full database
 * access to be running.
 * 
 * @author Dan Mascenik
 * 
 */
public class RemoteServiceIntegrationTests extends BaseSpringContextTestCase {

	@Autowired
	private SystemPropertyService svc;

	@Autowired
	private RemoteableService<SystemPropertyMessage, SystemProperty> systemPropertyService;

	@Test
	public void testSystemPropertyCrud() {
		long count = svc.count(new Query("", 0, 1));
		assertTrue(count > 0);
		count = svc.count(new Query("category: test", 0, 1));
		assertEquals(0, count);

		// Create a system property
		SystemProperty sp = new SystemProperty();
		sp.setCategory("test");
		sp.setDescription("Some test category");
		sp.setKey("my.prop");
		sp.setPrettyName("My Property");
		sp.setValue("yes");
		assertNull(sp.getGuid());
		assertNull(sp.getId());
		assertNull(sp.getVersion());
		svc.createEntity(sp);
		assertNotNull(sp.getGuid());
		// not provided to the remote consumer
		assertNull(sp.getId());
		assertNotNull(sp.getVersion());
		count = svc.count(new Query("category: test", 0, 1));
		assertEquals(1, count);

		// Get the property
		SystemProperty savedSp = svc.retrieveEntity(sp.getGuid());
		assertNotNull(savedSp);
		assertEquals(sp, savedSp);
		assertEquals(sp.getCategory(), savedSp.getCategory());
		assertEquals(sp.getDescription(), savedSp.getDescription());
		assertEquals(sp.getGuid(), savedSp.getGuid());
		assertEquals(sp.getKey(), savedSp.getKey());
		assertEquals(sp.getPrettyName(), savedSp.getPrettyName());
		assertEquals(sp.getValue(), savedSp.getValue());
		assertEquals(sp.getCreateDate().getTime(), savedSp.getCreateDate()
				.getTime());
		assertEquals(sp.getId(), savedSp.getId());
		assertEquals(sp.getVersion(), savedSp.getVersion());

		// Update the property
		savedSp.setValue("no");
		long v0 = savedSp.getVersion().longValue();
		svc.updateEntity(savedSp);
		assertEquals(v0 + 1l, savedSp.getVersion().longValue());
		SystemProperty updatedSp = svc.retrieveEntity(sp.getGuid());
		assertNotNull(updatedSp);
		assertEquals(savedSp.getVersion().longValue(), updatedSp.getVersion()
				.longValue());
		assertEquals("no", updatedSp.getValue());
		assertEquals(savedSp.getCategory(), updatedSp.getCategory());
		assertEquals(savedSp.getDescription(), updatedSp.getDescription());
		assertEquals(savedSp.getKey(), updatedSp.getKey());
		assertEquals(savedSp.getPrettyName(), updatedSp.getPrettyName());
		assertEquals(savedSp.getCreateDate().getTime(), savedSp.getCreateDate()
				.getTime());
		assertEquals(savedSp.getId(), updatedSp.getId());

		// Delete the property
		svc.deleteEntity(sp.getGuid());
		count = svc.count(new Query("category: test", 0, 1));
		assertEquals(0, count);
	}

	@Before
	public void before() throws Exception {
		systemPropertyService.setUseRemote(true);

		RestProtobufRemoteService<SystemPropertyMessage, SystemProperty> service = systemPropertyService
				.getRemoteService();
		service.setServiceUrl("http://localhost:8080/svc");
		service.setResourceStub("/internal/sysprop");
	}

}
