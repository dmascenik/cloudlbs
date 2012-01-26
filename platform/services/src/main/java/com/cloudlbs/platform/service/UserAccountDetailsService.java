package com.cloudlbs.platform.service;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.security.HeaderAuthenticationFilter;
import com.cloudlbs.core.utils.security.UserAccountDetails;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.domain.Role;
import com.cloudlbs.platform.domain.UserAccount;

/**
 * This is a Spring Security {@link UserDetailsService} for local authentication
 * purposes only (via embedded username/password form from Spring Security).
 * This should be disabled on a production system by setting the
 * <code>ignoreFailure</code> property of the {@link HeaderAuthenticationFilter}
 * to false.
 * 
 * @author Dan Mascenik
 * 
 */
public class UserAccountDetailsService implements UserDetailsService {

	private GenericService<UserAccount> userAccountService;

	@Autowired
	public UserAccountDetailsService(
			GenericService<UserAccount> userAccountService) {
		this.userAccountService = userAccountService;
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

		Query query = new Query("username: " + username, 0, 1);
		SearchResult<UserAccount> results = userAccountService.search(query);

		if (results.getTotalResults() != 1) {
			throw new UsernameNotFoundException("No user with username: "
					+ username);
		}

		UserAccount user = results.getValues().get(0);
		UserAccountDetails ud = new UserAccountDetails(user.getUsername(),
				user.getPassword(), null);
//		if (user.getScopeGuid() == null
//				|| user.getScopeGuid().trim().length() == 0) {
//			log.error("User " + user.getUsername()
//					+ " does not have a base scope! "
//					+ "Account will be disabled");
//			ud.setEnabled(false);
//		}
		ud.addAuthority("ROLE_USER");
		Iterator<Role> iter = user.getGrantedAuthoritys().iterator();
		while (iter.hasNext()) {
			ud.addAuthority(iter.next().getName());
		}
		ud.setGuid(user.getGuid());
		ud.setEmail(user.getEmail());
		ud.setFirstName(user.getFirstName());
		ud.setLastName(user.getLastName());

		return ud;

	}

	private Logger log = LoggerFactory
			.getLogger(UserAccountDetailsService.class);

}
