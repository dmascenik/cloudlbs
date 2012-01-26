package com.cloudlbs.platform.integration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.protocol.ProtobufMessageConverter;
import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.core.utils.remote.SecureRestTemplate;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.platform.BaseSpringContextTestCase;
import com.cloudlbs.platform.domain.UserAccount;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage.Builder;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;

/**
 * These tests require a localhost:8080 service instance with full database and
 * NO XMPP, and a localhost:8090 service instance with XMPP access and NO
 * database access to be running.
 * 
 * @author Dan Mascenik
 * 
 */
public class InternalUserAccountControllerTests extends
		BaseSpringContextTestCase {

	@Autowired
	SecureRestTemplate secureRestTemplate;

	RestProtobufRemoteService<UserAccountMessage, UserAccount> service;

	@Test
	public void testSearchByUsername() {
		Builder b = QueryMessage.newBuilder();
		b.setQ("username: admin");
		QueryMessage query = b.build();

		SearchResult<UserAccount> results = service.search(query);
		assertNotNull(results);
	}

	@Before
	public void before() throws Exception {
		MessageConverter<UserAccountMessage, UserAccount> messageConverter = new ProtobufMessageConverter<UserAccountMessage, UserAccount>(
				UserAccountMessage.class, UserAccount.class) {
		};
		service = new RestProtobufRemoteService<UserAccountMessage, UserAccount>(
				messageConverter, UserAccountMessage.items,
				UserAccountMessage.class, UserAccount.class, secureRestTemplate) {
		};
		service.setServiceUrl("http://localhost:8080/svc");
		service.setResourceStub("/internal/uacct");
	}
}
