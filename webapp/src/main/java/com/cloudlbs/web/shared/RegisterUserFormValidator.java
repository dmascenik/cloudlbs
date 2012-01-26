package com.cloudlbs.web.shared;

/**
 * @author Dan Mascenik
 * 
 */
public class RegisterUserFormValidator {

	/**
	 * Checks that username is just alphanumeric characters, at least 6 and no
	 * more than 20
	 * 
	 * @param username
	 */
	public static boolean isValidUsername(String username) {
		boolean result = true;
		if (username == null) {
			return false;
		}
		username = username.trim();
		if (username.length() < 6 || username.length() > 20) {
			result = false;
		}
		if (!username.matches("^[a-zA-Z0-9]+$")) {
			result = false;
		}
		return result;
	}

}
