package com.cloudlbs.platform.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.cloudlbs.core.utils.domain.DatabaseEntity;

/**
 * A {@link UserAccount} represents the identity of an "entity" that owns
 * devices and apps.<br/>
 * <br/>
 * This is the "owning" end of the many-to-many associations with {@link Device}
 * and {@link App}, so those sets will always be accessible from this class.
 * 
 * @author Dan Mascenik
 * 
 */
@Entity
@Table(name = "USER_ACCOUNT")
public class UserAccount extends DatabaseEntity {

	private static final long serialVersionUID = -2859138816987963288L;

	public static final String STATUS_OK = "OK";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_DISABLED = "DISABLED";
	public static final String STATUS_INACTIVE = "INACTIVE";

	@Column(name = "USERNAME", unique = true, nullable = false)
	private String username;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "DISPLAY_NAME")
	private String displayName;

	@Column(name = "EMAIL", nullable = false)
	private String email;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "STATUS", nullable = false)
	private String status;

	/**
	 * A {@link UserAccount} may be associated with N {@link Device}s
	 */
	@ManyToMany(targetEntity = Device.class)
	@JoinTable(name = "USER_DEVICE", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "DEVICE_ID", referencedColumnName = "ID"))
	private Set<Device> devices = new HashSet<Device>();

	/**
	 * A {@link UserAccount} may be associated with N {@link Role}s
	 */
	@ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
	@JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
	private Set<Role> grantedAuthoritys = new HashSet<Role>();

	public UserAccount() {
	}

	public UserAccount(String guid, String username) {
		this(guid);
		this.username = username;
	}

	public UserAccount(String guid) {
		super(guid);
	}

	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
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

	public Set<Role> getGrantedAuthoritys() {
		return grantedAuthoritys;
	}

	public void addRole(Role role) {
		grantedAuthoritys.add(role);
	}

	public void removeRole(Role role) {
		grantedAuthoritys.remove(role);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
