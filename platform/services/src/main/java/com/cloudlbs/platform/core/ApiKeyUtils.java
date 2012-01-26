package com.cloudlbs.platform.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * Some general functions for generating and validating API keys.
 * 
 * @author Dan Mascenik
 * 
 */
public class ApiKeyUtils {

	/**
	 * Generates a repeatable alphanumeric API key for the given parameters.
	 * Although statistically unlikely, this key is not guaranteed to be unique.
	 * If a key string is generated that is already in use, try again with a
	 * different random seed.
	 * 
	 * @param appIdentifier
	 * @param certHex
	 * @param seed
	 */
	public static String generateKeyString(String appIdentifier,
			String certHex, String seed) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		String raw = appIdentifier + certHex + seed;
		md.update(raw.getBytes());
		String rawKey = new String(Base64.encodeBase64(md.digest()));

		// Remove any non-word characters
		return rawKey.replaceAll("\\W", "");
	}

	/**
	 * Returns true if the provided key string matches the generated key for the
	 * other factors.
	 * 
	 * @param keyString
	 * @param appIdentifier
	 * @param certHex
	 * @param seed
	 */
	public static boolean isValid(String keyString, String appIdentifier,
			String certHex, String seed) {
		String generatedKey = generateKeyString(appIdentifier, certHex, seed);
		if (keyString.equals(generatedKey)) {
			return true;
		} else {
			return false;
		}
	}

}
