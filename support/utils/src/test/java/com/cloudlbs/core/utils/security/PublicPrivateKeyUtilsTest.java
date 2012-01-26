package com.cloudlbs.core.utils.security;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dan Mascenik
 * 
 */
public class PublicPrivateKeyUtilsTest extends Assert {

	String pubKey;
	String privKey;

	@Test
	public void testLoadPublicKeyFromString() throws Exception {
		PublicKey publicKey = PublicPrivateKeyUtils.getPublicKey(pubKey);
		assertNotNull(publicKey);
	}

	@Test
	public void testLoadPrivateKeyFromString() throws Exception {
		PrivateKey privateKey = PublicPrivateKeyUtils.getPrivateKey(privKey);
		assertNotNull(privateKey);
	}

	@Test
	public void testLoadPublicKeyFromStream() throws Exception {
		byte[] bytes = pubKey.getBytes();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		PublicKey publicKey = PublicPrivateKeyUtils.getPublicKey(in);
		assertNotNull(publicKey);
	}

	@Test
	public void testLoadPrivateKeyFromStream() throws Exception {
		byte[] bytes = privKey.getBytes();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		PrivateKey privateKey = PublicPrivateKeyUtils.getPrivateKey(in);
		assertNotNull(privateKey);
	}

	@Test
	public void testLoadPublicKeyFromStreamWithWhitespace() throws Exception {
		byte[] bytes = ("   " + pubKey.substring(0, 10) + "\n\n  \t"
				+ pubKey.substring(10, pubKey.length()) + "  ").getBytes();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		PublicKey publicKey = PublicPrivateKeyUtils.getPublicKey(in);
		assertNotNull(publicKey);
	}

	@Test
	public void testVerifySignature() throws Exception {
		String message = "here's a message";
		PrivateKey privateKey = PublicPrivateKeyUtils.getPrivateKey(privKey);
		String signature = PublicPrivateKeyUtils.sign(message, privateKey);
		assertNotNull(signature);

		PublicKey publicKey = PublicPrivateKeyUtils.getPublicKey(pubKey);
		assertTrue(PublicPrivateKeyUtils.verify(signature, message, publicKey));
	}

	@Before
	public void before() {
		String[] keys = PublicPrivateKeyUtils
				.generatePublicPrivateKeyPairStrings();

		pubKey = keys[0];
		privKey = keys[1];
	}

	@After
	public void after() {
		pubKey = null;
		privKey = null;
	}

}
