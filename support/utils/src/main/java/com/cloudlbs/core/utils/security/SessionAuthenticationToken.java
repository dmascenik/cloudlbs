package com.cloudlbs.core.utils.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Dan Mascenik
 * 
 */
public class SessionAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -3952137173236583420L;

	public static final String SESSION_HEADER = "X-SessionID";

	private String sessionId;
	private String username;

	public SessionAuthenticationToken(String sessionId) {
		super(null);
		this.sessionId = sessionId;
	}

	public SessionAuthenticationToken(String username, String sessionId,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.username = username;
		this.sessionId = sessionId;
	}

	/**
	 * Returns the sessionId String
	 */
	@Override
	public Object getCredentials() {
		return sessionId;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	public String getSessionId() {
		return sessionId;
	}
}
