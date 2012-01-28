package com.cloudlbs.web.server.remote;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.web.server.BaseSpringContextTestCase;
import com.cloudlbs.web.shared.dto.UserAccountDTO;

/**
 * Requires the accounts service to be running at localhost:9010/accounts
 * 
 * @author Dan Mascenik
 * 
 */
public class UserAccountRemoteTests extends BaseSpringContextTestCase {

	@Autowired
	UserAccountRemote userServiceRemote;

	@Test
	public void testSearchForUsername() {
		QueryMessage.Builder b = QueryMessage.newBuilder();
		b.setQ("username: admin");
		SearchResult<UserAccountDTO> result = userServiceRemote.search(b
				.build());

		assertNotNull(result);
		assertEquals(1, result.getTotalResults());
	}

	@Test
	public void testCreateUser() {
		UserAccountDTO user = new UserAccountDTO();
		user.setDisplayName("Some Dude");
		user.setUsername("somedude");
		user.setEmail("some@dude.com");
		user.setPassword("abc123");

		UserAccountDTO newUser = userServiceRemote.create(user);
		assertNotNull(newUser);
		assertNotNull(newUser.getGuid());

		QueryMessage.Builder b = QueryMessage.newBuilder();
		b.setQ("username: somedude");
		SearchResult<UserAccountDTO> result = userServiceRemote.search(b
				.build());

		assertNotNull(result);
		assertEquals(1, result.getTotalResults());

	}

}
