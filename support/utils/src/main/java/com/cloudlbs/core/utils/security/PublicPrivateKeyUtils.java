package com.cloudlbs.core.utils.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

/**
 * This utility class provides convenient access to public/private RSA key
 * signing/verification algorithms provided by the Bouncy Castle Security
 * Provider.
 * 
 * @author Dan Mascenik
 * 
 */
public class PublicPrivateKeyUtils {

	/**
	 * Generate a public/private key pair and return them as a pair of base-64
	 * encoded strings.
	 * 
	 * @return array index 0 contains the public key bytes, array index 1
	 *         contains the private key bytes
	 */
	public static String[] generatePublicPrivateKeyPairStrings() {
		KeyPair pair = generatePublicPrivateKeyPair();
		PublicKey pubKey = pair.getPublic();
		PrivateKey privKey = pair.getPrivate();
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKey.getEncoded());
		String pubKeyString = new String(Base64.encode(pubSpec.getEncoded()));
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(
				privKey.getEncoded());
		String privKeyString = new String(Base64.encode(privSpec.getEncoded()));
		return new String[] { pubKeyString, privKeyString };
	}

	/**
	 * Generates a public/private key pair.
	 */
	public static KeyPair generatePublicPrivateKeyPair() {
		init();
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA", "BC");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("BouncyCastleProvider is not available",
					e);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException("BouncyCastleProvider is not available",
					e);
		}
		keyGen.initialize(512, new SecureRandom());
		return keyGen.generateKeyPair();
	}

	/**
	 * Verifies that the signature was made for the given message using the
	 * private half of the given {@link PublicKey}
	 * 
	 * @param signature
	 *            base-64 encoded signature bytes
	 * @param message
	 * @param key
	 */
	public static boolean verify(String signature, byte[] message, PublicKey key) {
		return verify(Base64.decode(signature), message, key);
	}

	/**
	 * Verifies that the signature was made for the given message using the
	 * private half of the given {@link PublicKey}
	 * 
	 * @param signature
	 *            base-64 encoded signature bytes
	 * @param message
	 * @param key
	 */
	public static boolean verify(String signature, String message, PublicKey key) {
		return verify(Base64.decode(signature), message.getBytes(), key);
	}

	/**
	 * Verifies that the signature was made for the given message using the
	 * private half of the given {@link PublicKey}
	 * 
	 * @param signature
	 * @param message
	 * @param key
	 */
	public static boolean verify(byte[] sigBytes, byte[] message, PublicKey key) {
		init();
		try {
			Signature signature = Signature.getInstance("SHA1withRSA", "BC");
			signature.initVerify(key);
			signature.update(message);
			return signature.verify(sigBytes);
		} catch (Exception e) {
			throw new RuntimeException("Could not verify signature", e);
		}
	}

	/**
	 * Signs the message using the {@link PrivateKey} and returns the resulting
	 * bytes as a base-64 encoded String.
	 * 
	 * @param message
	 * @param key
	 */
	public static String sign(String message, PrivateKey key) {
		return sign(message.getBytes(), key);
	}

	/**
	 * Signs the message using the {@link PrivateKey} and returns the resulting
	 * bytes as a base-64 encoded String.
	 * 
	 * @param message
	 * @param key
	 */
	public static String sign(byte[] message, PrivateKey key) {
		init();
		try {
			Signature signature = Signature.getInstance("SHA1withRSA", "BC");
			signature.initSign(key, new SecureRandom());
			signature.update(message);
			return new String(Base64.encode(signature.sign()));
		} catch (Exception e) {
			throw new RuntimeException("Failed to sign message", e);
		}
	}

	/**
	 * Returns the {@link PublicKey} from a {@link File}.
	 * 
	 * @see X509EncodedKeySpec
	 * @param keyString
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(File keyFile)
			throws InvalidKeySpecException {
		try {
			return getPublicKey(new FileInputStream(keyFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Failed to read key from file", e);
		}
	}

	/**
	 * Returns the {@link PrivateKey} from a {@link File}.
	 * 
	 * @see PKCS8EncodedKeySpec
	 * @param keyString
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(File keyFile)
			throws InvalidKeySpecException {
		try {
			return getPrivateKey(new FileInputStream(keyFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Failed to read key from file", e);
		}
	}

	/**
	 * Returns the {@link PublicKey} from an {@link InputStream}.
	 * 
	 * @see X509EncodedKeySpec
	 * @param keyString
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(InputStream in)
			throws InvalidKeySpecException {
		try {
			return getPublicKey(streamToString(in));
		} catch (IOException e) {
			throw new RuntimeException("Failed to read key from stream", e);
		}
	}

	/**
	 * Returns the {@link PrivateKey} from an {@link InputStream}.
	 * 
	 * @see PKCS8EncodedKeySpec
	 * @param keyString
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(InputStream in)
			throws InvalidKeySpecException {
		try {
			return getPrivateKey(streamToString(in));
		} catch (IOException e) {
			throw new RuntimeException("Failed to read key from stream", e);
		}
	}

	/**
	 * Returns the {@link PublicKey} for the provided base-64 encoded bytes.
	 * 
	 * @see X509EncodedKeySpec
	 * @param keyString
	 *            base-64 encoded key bytes
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(String keyString)
			throws InvalidKeySpecException {
		return getPublicKey(Base64.decode(keyString));
	}

	/**
	 * Returns the {@link PrivateKey} for the provided base-64 encoded bytes.
	 * 
	 * @see PKCS8EncodedKeySpec
	 * @param keyString
	 *            base-64 encoded key bytes
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(String keyString)
			throws InvalidKeySpecException {
		return getPrivateKey(Base64.decode(keyString));
	}

	/**
	 * Returns the {@link PublicKey} for the provided encoded bytes.
	 * 
	 * @see X509EncodedKeySpec
	 * @param encodedBytes
	 */
	public static PublicKey getPublicKey(byte[] encodedBytes)
			throws InvalidKeySpecException {
		init();
		KeySpec pubKeySpec = new X509EncodedKeySpec(encodedBytes);
		KeyFactory factory;
		try {
			factory = KeyFactory.getInstance("RSA", "BC");
		} catch (NoSuchProviderException e) {
			throw new RuntimeException("BouncyCastleProvider is not available",
					e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("BouncyCastleProvider is not available",
					e);
		}
		return factory.generatePublic(pubKeySpec);
	}

	/**
	 * Returns the {@link PrivateKey} for the provided encoded bytes.
	 * 
	 * @see PKCS8EncodedKeySpec
	 * @param encodedBytes
	 */
	public static PrivateKey getPrivateKey(byte[] encodedBytes)
			throws InvalidKeySpecException {
		init();
		KeySpec privKeySpec = new PKCS8EncodedKeySpec(encodedBytes);
		KeyFactory factory;
		try {
			factory = KeyFactory.getInstance("RSA", "BC");
		} catch (NoSuchProviderException e) {
			throw new RuntimeException("BouncyCastleProvider is not available",
					e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("BouncyCastleProvider is not available",
					e);
		}
		return factory.generatePrivate(privKeySpec);
	}

	private static void init() {
		/*
		 * This gets called by every invocation. It does not actually add the
		 * provider again if it has already been added
		 */
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * This expects the input stream to contain a base-64 encoded string,
	 * possibly with whitespace characters peppered throughout (e.g. new lines,
	 * tabs, etc). This will clean the string, then base-64 decode it.
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	static String streamToString(InputStream in) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int read = 0;
		while ((read = in.read(buff)) > -1) {
			os.write(buff, 0, read);
		}
		String rawString = os.toString();
		return rawString.replaceAll("\\s", "");
	}
}
