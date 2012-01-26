package com.cloudlbs.web.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.remote.RemoteService;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;
import com.cloudlbs.web.client.UserAccountService;
import com.cloudlbs.web.shared.dto.UserAccountDTO;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Dan Mascenik
 * 
 */
@Service("user")
public class UserAccountServiceImpl extends RemoteServiceServlet implements
		UserAccountService {

	@Autowired
	private RemoteService<UserAccountMessage, UserAccountDTO> userAccountRemote;

	public List<UserAccountDTO> search(String query) {
		QueryMessage.Builder b = QueryMessage.newBuilder();
		b.setQ(query);
		SearchResult<UserAccountDTO> results = userAccountRemote.search(b
				.build());
		return results.getValues();
	}

	public UserAccountDTO create(UserAccountDTO representation) {
		return userAccountRemote.create(representation);
	}

}
