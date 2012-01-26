package com.cloudlbs.web.shared.validation;

/**
 * @author Dan Mascenik
 * 
 */
public class UserFieldsValidator {

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

	public static boolean isValidPassword(String password) {
		boolean result = true;
		if (password == null) {
			return false;
		}
		password = password.trim();
		if (password.length() < 6 || password.length() > 20) {
			result = false;
		}
		if (!password.matches("^[a-zA-Z0-9!@#$%^&*()-=_+,.?<>~]+$")) {
			// Something other than alphanumeric and punctuation
			result = false;
		}
		if (password.matches("^[a-zA-Z]*$")) {
			// all letters
			result = false;
		}
		if (password.matches("^[0-9]*$")) {
			// all numbers
			result = false;
		}

		return result;
	}

	/**
	 * Validates that the text is just alphanumeric characters or numbers. Null
	 * or empty is ok, too. Text cannot be longer than 256 chars.
	 * 
	 * @param text
	 */
	public static boolean isValidText(String text) {
		boolean result = true;
		if (text == null || text.trim().length() == 0) {
			return true;
		}
		text = text.trim();
		if (text.length() > 256) {
			result = false;
		}
		if (!text.matches("^[a-zA-Z0-9 ]+$")) {
			result = false;
		}
		return result;
	}

	public static boolean isValidEmail(String email) {
		boolean result = true;
		if (email == null) {
			return false;
		}
		email = email.trim();
		if (email.length() > 256) {
			result = false;
		}
		if (!email.toUpperCase().matches(
				"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$")) {
			result = false;
		}
		return result;
	}

	public static boolean isNull(String value) {
		return (value == null || value.trim().length() == 0);
	}

	public static boolean isEqual(String value1, String value2) {
		return (value1 != null && value2 != null && value1.trim().equals(
				value2.trim()));
	}
}
