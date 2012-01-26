package com.cloudlbs.platform.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.BaseSpringContextTestCase;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage.Builder;

public class ApiKeyValidationServiceTest extends BaseSpringContextTestCase {

	@Autowired
	GenericService<AppDetailsMessage> apiKeyValidationService;

	@Test
	@Transactional
	public void testValidateApiKey() {
		Builder b = AppDetailsMessage.newBuilder();
		b.setApiKey("pbJS6bLTQD3oEieKQWl7ltrdvU");
		b.setAppIdentifier("com.cloudlbs.whereru");
		b.setCertificateFingerprint("ABCDEFG");
		b.setDeviceUniqueId("12345");
		AppDetailsMessage adm = b.build();

		AppDetailsMessage out = apiKeyValidationService.createEntity(adm);
		assertTrue(out.getIsValid());

		Builder badKey = AppDetailsMessage.newBuilder();
		badKey.setApiKey("badKey");
		badKey.setAppIdentifier("com.cloudlbs.whereru");
		badKey.setCertificateFingerprint("ABCDEFG");
		badKey.setDeviceUniqueId("12345");
		adm = badKey.build();

		out = apiKeyValidationService.createEntity(adm);
		assertFalse(out.getIsValid());

		Builder wrongApp = AppDetailsMessage.newBuilder();
		wrongApp.setApiKey("pbJS6bLTQD3oEieKQWl7ltrdvU");
		wrongApp.setAppIdentifier("com.cloudlbs.otherApp");
		wrongApp.setCertificateFingerprint("ABCDEFG");
		wrongApp.setDeviceUniqueId("12345");
		adm = wrongApp.build();

		out = apiKeyValidationService.createEntity(adm);
		assertFalse(out.getIsValid());
	}

}
