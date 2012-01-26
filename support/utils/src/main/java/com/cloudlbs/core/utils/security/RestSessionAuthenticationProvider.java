package com.cloudlbs.core.utils.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.cloudlbs.core.utils.remote.RemoteService;
import com.cloudlbs.platform.protocol.AuthenticationProto.AuthenticationStatus;
import com.cloudlbs.platform.protocol.AuthenticationProto.SessionAuthenticationMessage;

/**
 * Handles {@link SessionAuthenticationToken}s and delegates authentication
 * calls to a {@link ProtobufRemoteService} that takes
 * {@link SessionAuthenticationMessage}s.
 * 
 * @author Dan Mascenik
 * 
 */
public class RestSessionAuthenticationProvider extends
		AbstractRestAuthenticationProvider<SessionAuthenticationMessage> {

	/**
	 * 
	 * @param sessionAuthenticationService
	 */
	@Autowired
	public RestSessionAuthenticationProvider(
			RemoteService<SessionAuthenticationMessage, SessionAuthenticationMessage> sessionAuthenticationService) {
		super(sessionAuthenticationService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {

		SessionAuthenticationToken authentication = (SessionAuthenticationToken) auth;

		// Call the remote authentication service
		SessionAuthenticationMessage sam;
		try {
			sam = remoteAuthenticationService
					.get(authentication.getSessionId());
		} catch (Exception e) {
			throw new RuntimeException("Service call to remote "
					+ "authentication service failed", e);
		}

		if (sam.getStatus().equals(AuthenticationStatus.FAILED)) {
			if (sam.hasFailedReason()) {
				log.debug("Authentication service sent failure message: "
						+ sam.getFailedReason());
				log.debug("SessionAuthenticationMessage:\n" + sam.toString());
			}
			if (sam.getScopeGuid() == null
					|| sam.getScopeGuid().trim().length() == 0) {
				log.error("User " + sam.getUsername()
						+ " does not have a base scope! "
						+ "Account will be disabled");
				throw new DisabledException("No base scope for user "
						+ sam.getUsername());
			}
			throw new BadCredentialsException("Invalid session");
		}

		UserAccountDetails ad = new UserAccountDetails(sam.getUsername(),
				sam.getPassword(), sam.getScopeGuid());

		for (int i = 0; i < sam.getGrantedAuthorityCount(); i++) {
			final String authority = sam.getGrantedAuthority(i);
			ad.addAuthority(authority);
		}

		SessionAuthenticationToken st = new SessionAuthenticationToken(
				sam.getUsername(), sam.getSessionId(), ad.getAuthorities());
		st.setDetails(ad);

		return st;
	}

	/**
	 * Handles the {@link SessionAuthenticationToken}
	 */
	@Override
	public boolean supports(Class<?> authClass) {
		return SessionAuthenticationToken.class.isAssignableFrom(authClass);
	}

	private Logger log = LoggerFactory
			.getLogger(RestSessionAuthenticationProvider.class);

}
