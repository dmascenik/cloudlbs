package com.cloudlbs.sls.utils;

public class UserCredentials {

	private String username;
	private String password;
	private String token;
	private long expirationTime;

	public UserCredentials(String username, String password, String token) {
		this.username = username;
		this.password = password;
		this.token = token;
		this.expirationTime = System.currentTimeMillis() + (15l * 60l * 1000l);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getToken() {
		return token;
	}

	/**
	 * Expiration time is always 15 minutes from initial creation
	 */
	public long getExpirationTime() {
		return expirationTime;
	}

}
