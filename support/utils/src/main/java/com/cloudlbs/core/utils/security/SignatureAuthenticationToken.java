package com.cloudlbs.core.utils.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Dan Mascenik
 * 
 */
public class SignatureAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 5129043939940437227L;

	public static final String CLIENT_ID_HEADER = "X-ClientID";
	public static final String SIGNATURE_HEADER = "X-Signature";

	private Object principal;
	private String clientId;
	private String signature; // should be base-64 encoded
	private String message;

	/**
	 * 
	 * @param clientId
	 *            ServiceAccount GUID
	 * @param signature
	 *            base-64 encoded signature bytes
	 * @param message
	 */
	public SignatureAuthenticationToken(String clientId, String signature,
			String message) {
		super(null);
		this.clientId = clientId;
		this.signature = signature;
		this.message = message;
	}

	public SignatureAuthenticationToken(Object principal, String clientId,
			String signature, String message,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.clientId = clientId;
		this.signature = signature;
		this.message = message;
		this.principal = principal;
	}

	@Override
	public Object getCredentials() {
		return signature;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public String getClientId() {
		return clientId;
	}

	public String getMessage() {
		return message;
	}

	public String getSignature() {
		return signature;
	}
}
