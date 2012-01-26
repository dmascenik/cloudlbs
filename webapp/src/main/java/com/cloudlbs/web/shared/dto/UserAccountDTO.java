package com.cloudlbs.web.shared.dto;

import java.io.Serializable;

/**
 * @author Dan Mascenik
 * 
 */
public class UserAccountDTO implements Serializable {

	public static final String STATUS_OK = "OK";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_DISABLED = "DISABLED";
	public static final String STATUS_INACTIVE = "INACTIVE";

	private String guid;
	private String username;
	private String firstName;
	private String lastName;
	private String displayName;
	private String email;
	private String password;
	private String status;
	private Long version;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
