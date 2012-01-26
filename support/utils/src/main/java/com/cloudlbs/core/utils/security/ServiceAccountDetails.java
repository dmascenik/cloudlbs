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
public class ServiceAccountDetails implements UserDetails,
		ScopeRestrictedAccount {

	private static final long serialVersionUID = -6894585299579387083L;

	private String clientId;
	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	private String baseScopeGuid;

	private boolean accountExpired = false;
	private boolean accountLocked = false;
	private boolean credentialsExpired = false;
	private boolean enabled = true;

	public ServiceAccountDetails(String clientId, String baseScopeGuid) {
		this.clientId = clientId;
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
	 * org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {
		return "ServiceAccount: " + clientId;
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

	@Override
	public String getPassword() {
		return "[SIGNATURE]";
	}
}
