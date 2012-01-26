package com.cloudlbs.core.utils.service;

import java.security.PrivateKey;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cloudlbs.core.utils.remote.RemoteService;
import com.cloudlbs.core.utils.security.PublicPrivateKeyUtils;
import com.cloudlbs.core.utils.security.SessionAuthenticationToken;
import com.cloudlbs.core.utils.security.SignatureAuthenticationToken;
import com.cloudlbs.platform.protocol.AuthenticationProto.AuthenticationStatus;
import com.cloudlbs.platform.protocol.AuthenticationProto.SessionAuthenticationMessage;

/**
 * @author Dan Mascenik
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContextRest.xml" })
public class RemoteAuthProviderTests extends Assert {

	String myservicePrivateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAtHuiyKzOJwXW4"
			+ "+Zo/S6SGihNC0ppkZW/K42GRRRCuOBjZswZ4AyW/T2ixC6zbCruGDQSOvTG4VaSDhLil5Y"
			+ "bkQIDAQABAkEAqHN8y6/9+Y4Js0wqUaRV2PQkdJVPUwJhG1VMLM1sOQAfwYfk7UzmHfmAa"
			+ "b/gMxRxwXLtzURIGLhhBwNpCpXpwQIhAPrB8fNKRQ1zJMahx8w2YCuYuOuniBBxpMmj8eP"
			+ "LadMvAiEAuEGUrtVR3mc1/t/oCIueizqOcG907TmE8Zl0B4LaTT8CIQCkpx8RKm5nI3k9eF"
			+ "NyMy440kJycoI0kfqrcJxPgGzPfwIgZ6Ga+mpITYpHOD6+xm+gkDYy/tHxNatwNmJUfBUA"
			+ "qwkCIQD3EgSE420NdG4KTpxmciMlOB3vgJ8hSe4siv5SIHmqKw==";

	@Autowired
	AuthenticationProvider sessionAuthenticationProvider;

	@Autowired
	AuthenticationProvider signatureAuthenticationProvider;

	@Autowired
	RemoteService<SessionAuthenticationMessage, SessionAuthenticationMessage> sessionAuthenticationService;

	@Test
	public void testAuthenticateSignatureToken() throws Exception {
		String message = "my message";
		PrivateKey key = PublicPrivateKeyUtils
				.getPrivateKey(myservicePrivateKey);
		String signature = PublicPrivateKeyUtils.sign(message, key);

		SignatureAuthenticationToken token = new SignatureAuthenticationToken(
				"testservice", signature, message);
		Authentication auth = signatureAuthenticationProvider
				.authenticate(token);
		assertNotNull(auth);
		assertNotNull(auth.getAuthorities());
	}

	@Test
	public void testSessionAuthentication() throws Exception {
		SessionAuthenticationMessage.Builder b = SessionAuthenticationMessage
				.newBuilder();
		b.setUsername("admin");
		b.setPassword("password");
		SessionAuthenticationMessage samResponse = sessionAuthenticationService
				.create(b.build());

		assertEquals(AuthenticationStatus.SUCCESS, samResponse.getStatus());
		String sessionId = samResponse.getSessionId();

		SessionAuthenticationToken token = new SessionAuthenticationToken(
				sessionId);
		Authentication auth = sessionAuthenticationProvider.authenticate(token);
		assertNotNull(auth);
		assertNotNull(auth.getAuthorities());
	}
}
