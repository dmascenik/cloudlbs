package com.cloudlbs.core.utils.remote;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.HttpMessageConverter;

import com.cloudlbs.core.utils.security.PublicPrivateKeyUtils;
import com.cloudlbs.core.utils.test.ItemProto;
import com.cloudlbs.platform.protocol.AuthenticationProto.SessionAuthenticationMessage;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.platform.protocol.SearchProto.SearchResultMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class SecureRestTemplateTests extends Assert {

	SecureRestTemplate template;
	PrivateKey key;
	String baseUrl = "http://localhost:9020/locations";
	String urlStub = "/scope/query";
	QueryMessage query;

	@Test
	public void testPostForObjectWithSignature() {
		SearchResultMessage m = template.postForObject("SYSTEM", key, baseUrl,
				urlStub, query, SearchResultMessage.class);

		// Looking up root scope - should be only 1
		assertEquals(1, m.getTotalResults());
	}

	@Test
	public void testPostForObjectWithSession() {
		SessionAuthenticationMessage.Builder b = SessionAuthenticationMessage
				.newBuilder();
		b.setUsername("admin");
		b.setPassword("password");
		SessionAuthenticationMessage sam = template.postForObject(
				"http://localhost:9010/accounts/internal/auth", b.build(),
				SessionAuthenticationMessage.class);
		String sessionId = sam.getSessionId();

		SearchResultMessage m = template.postForObject(sessionId, baseUrl
				+ urlStub, query, SearchResultMessage.class);

		// Looking up root scope - should be only 1
		assertEquals(1, m.getTotalResults());
	}

	@Before
	public void before() throws Exception {
		template = new SecureRestTemplate();

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ProtobufHttpMessageConverter(
				ItemProto.ItemMessage.items));
		template.setMessageConverters(messageConverters);

		key = PublicPrivateKeyUtils
				.getPrivateKey("MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAtHuiyKzOJwXW4"
						+ "+Zo/S6SGihNC0ppkZW/K42GRRRCuOBjZswZ4AyW/T2ixC6zbCruGDQSOvTG4VaSDhLil5Y"
						+ "bkQIDAQABAkEAqHN8y6/9+Y4Js0wqUaRV2PQkdJVPUwJhG1VMLM1sOQAfwYfk7UzmHfmAa"
						+ "b/gMxRxwXLtzURIGLhhBwNpCpXpwQIhAPrB8fNKRQ1zJMahx8w2YCuYuOuniBBxpMmj8eP"
						+ "LadMvAiEAuEGUrtVR3mc1/t/oCIueizqOcG907TmE8Zl0B4LaTT8CIQCkpx8RKm5nI3k9eF"
						+ "NyMy440kJycoI0kfqrcJxPgGzPfwIgZ6Ga+mpITYpHOD6+xm+gkDYy/tHxNatwNmJUfBUA"
						+ "qwkCIQD3EgSE420NdG4KTpxmciMlOB3vgJ8hSe4siv5SIHmqKw==");

		QueryMessage.Builder b = QueryMessage.newBuilder();
		b.setQ("parent: NULL");
		query = b.build();
	}

	@After
	public void after() {
		template = null;
		key = null;
		query = null;
	}

}
