package com.cloudlbs.core.utils.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class RestUserAccountDetailsService extends
		RestProtobufRemoteService<UserAccountMessage, UserAccountMessage>
		implements UserDetailsService {

	public RestUserAccountDetailsService() {
		super(null, UserAccountMessage.items);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		QueryMessage.Builder qb = QueryMessage.newBuilder();
		qb.setQ("username: " + username);

		SearchResult<UserAccountMessage> results = search(qb.build());

		if (results.getTotalResults() != 1) {
			throw new UsernameNotFoundException("No user with username: "
					+ username);
		}

		UserAccountMessage user = results.getValues().get(0);
		UserAccountDetails ud = new UserAccountDetails(user.getUsername(),
				user.getPassword(), null);
//		if (user.getScopeGuid() == null
//				|| user.getScopeGuid().trim().length() == 0) {
//			log.error("User " + user.getUsername()
//					+ " does not have a base scope! "
//					+ "Account will be disabled");
//			ud.setEnabled(false);
//		}
//		ud.addAuthority("ROLE_USER");
//		for (int i = 0; i < user.getGrantedAuthorityCount(); i++) {
//			ud.addAuthority(user.getGrantedAuthority(i));
//		}
		ud.setGuid(user.getGuid());
		return ud;
	}

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory
			.getLogger(RestUserAccountDetailsService.class);

}
