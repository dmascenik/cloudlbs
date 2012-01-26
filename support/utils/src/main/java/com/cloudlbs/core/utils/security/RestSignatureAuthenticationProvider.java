package com.cloudlbs.core.utils.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cloudlbs.core.utils.remote.RemoteService;
import com.cloudlbs.platform.protocol.AuthenticationProto.AuthenticationStatus;
import com.cloudlbs.platform.protocol.AuthenticationProto.SignatureAuthenticationMessage;

/**
 * Handles {@link SignatureAuthenticationToken}s and delegates authentication
 * calls to a {@link ProtobufRemoteService} that takes
 * {@link SignatureAuthenticationMessage}s.
 * 
 * @author Dan Mascenik
 * 
 */
public class RestSignatureAuthenticationProvider extends
		AbstractRestAuthenticationProvider<SignatureAuthenticationMessage> {

	/**
	 * 
	 * @param signatureAuthenticationService
	 */
	@Autowired
	public RestSignatureAuthenticationProvider(
			RemoteService<SignatureAuthenticationMessage, SignatureAuthenticationMessage> signatureAuthenticationService) {
		super(signatureAuthenticationService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@SuppressWarnings("serial")
	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {

		SignatureAuthenticationToken authentication = (SignatureAuthenticationToken) auth;

		SignatureAuthenticationMessage.Builder sb = SignatureAuthenticationMessage
				.newBuilder();
		sb.setClientId(authentication.getClientId());
		sb.setSignature(authentication.getSignature());
		sb.setMessage(authentication.getMessage());

		// Call the remote authentication service
		SignatureAuthenticationMessage sam;
		try {
			sam = remoteAuthenticationService.create(sb.build());
		} catch (Exception e) {
			throw new RuntimeException("Service call to remote "
					+ "authentication service failed", e);
		}

		if (sam.getStatus().equals(AuthenticationStatus.FAILED)) {
			if (sam.hasFailedReason()) {
				log.debug("Signature authentication service sent failure message: "
						+ sam.getFailedReason());
			}
			if (sam.hasNoSuchClient() && sam.getNoSuchClient()) {
				throw new UsernameNotFoundException("No client with id "
						+ sam.getClientId());
			}
			if (sam.hasAccountDisabled() && sam.getAccountDisabled()) {
				throw new DisabledException("Client account is disabled "
						+ sam.getClientId());
			}
			if (sam.hasInvalidSignature() && sam.getInvalidSignature()) {
				throw new BadCredentialsException("Invalid signature");
			}
			if (sam.hasScopeGuid() && sam.getScopeGuid().trim().length() == 0) {
				log.error("Client has no base scope GUID! Account will be disabled");
				throw new DisabledException("No base scope for client "
						+ sam.getClientId());
			}
			throw new AuthenticationException(
					"Unknown authentication exception") {
			};
		}

		ServiceAccountDetails ad = new ServiceAccountDetails(sam.getClientId(),
				sam.getScopeGuid());

		for (int i = 0; i < sam.getGrantedAuthorityCount(); i++) {
			final String authority = sam.getGrantedAuthority(i);
			ad.addAuthority(authority);
		}

		SignatureAuthenticationToken authOut = new SignatureAuthenticationToken(
				ad, sam.getClientId(), sam.getSignature(), sam.getMessage(),
				ad.getAuthorities());
		authOut.setDetails(ad);

		return authOut;
	}

	/**
	 * Handles the {@link SignatureAuthenticationToken}
	 */
	@Override
	public boolean supports(Class<?> authClass) {
		return SignatureAuthenticationToken.class.isAssignableFrom(authClass);
	}

	private Logger log = LoggerFactory
			.getLogger(RestSignatureAuthenticationProvider.class);

}
