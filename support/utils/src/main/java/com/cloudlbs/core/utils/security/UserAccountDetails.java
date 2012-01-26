package com.cloudlbs.core.utils.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Dan Mascenik
 * 
 */
public class UserAccountDetails implements UserDetails, ScopeRestrictedAccount {

	private static final long serialVersionUID = 5067508765309510984L;

	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String guid;
	private String password;
	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	private String baseScopeGuid;

	private boolean accountExpired = false;
	private boolean accountLocked = false;
	private boolean credentialsExpired = false;
	private boolean enabled = true;

	public UserAccountDetails(String username, String password,
			String baseScopeGuid) {
		this.username = username;
		this.password = password;
		this.baseScopeGuid = baseScopeGuid;
	}

	public void addAuthority(GrantedAuthority authority) {
		authorities.add(authority);
	}

	@SuppressWarnings("serial")
	public void addAuthority(final String authority) {
		authorities.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
				return authority;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getAuthorities
	 * ()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired
	 * ()
	 */
	@Override
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked
	 * ()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#
	 * isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getBaseScopeGuid() {
		return baseScopeGuid;
	}

	public void setBaseScopeGuid(String baseScopeGuid) {
		this.baseScopeGuid = baseScopeGuid;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
