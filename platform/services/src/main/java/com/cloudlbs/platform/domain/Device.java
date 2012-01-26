package com.cloudlbs.platform.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cloudlbs.core.utils.domain.DatabaseEntity;

/**
 * A {@link Device} is a thing, typically a smart phone, that can run apps. It
 * may be associated with multiple {@link UserAccount}s, since it is acceptable
 * for different users to operate different apps on the same device. <br/>
 * <br/>
 * NOTE: This is the <i>inverse</i> end of the many-to-many relationship with
 * {@link UserAccount}s, so the {@link #userAccounts} set will not be populated
 * on load of this entity. Likewise, changes to UserAccounts from this entity
 * will not be persisted.
 * 
 * @author Dan Mascenik
 */
@Entity
@Table(name = "DEVICE", uniqueConstraints = { @UniqueConstraint(columnNames = { "DEVICEUNIQUEID" }) })
public class Device extends DatabaseEntity {

	private static final long serialVersionUID = -4663959135174839390L;

	@Column(name = "DEVICEUNIQUEID", nullable = false)
	private String deviceUniqueId;

	@Column(name = "XMPP_USERNAME")
	private String xmppUsername;

	@Column(name = "XMPP_PASSWORD")
	private String xmppPassword;

	/**
	 * A {@link Device} may be associated with N {@link UserAccount}s
	 */
	@ManyToMany(mappedBy = "devices")
	@SuppressWarnings("unused")
	private Set<UserAccount> userAccounts = new HashSet<UserAccount>();

	/**
	 * A {@link Device} may be associated with N {@link App}s
	 */
	// @Fetch(FetchMode.SUBSELECT)
	// @ManyToMany(targetEntity =
	// com.cloudlbs.platform.account.domain.App.class, fetch = FetchType.EAGER)
	// @JoinTable(name = "DEVICE_APP", joinColumns = @JoinColumn(name =
	// "DEVICE_ID", referencedColumnName = "ID"), inverseJoinColumns =
	// @JoinColumn(name = "APP_ID", referencedColumnName = "ID"))
	// private Set<App> apps = new HashSet<App>();

	public Device() {
		super();
	}

	public Device(String guid) {
		super(guid);
	}

	public Device(String guid, String deviceUniqueId) {
		super(guid);
		this.deviceUniqueId = deviceUniqueId;
	}

	// public Set<App> getApps() {
	// return apps;
	// }
	//
	// public void setApps(Set<App> apps) {
	// this.apps = apps;
	// }

	public String getDeviceUniqueId() {
		return deviceUniqueId;
	}

	public void setDeviceUniqueId(String deviceUniqueId) {
		this.deviceUniqueId = deviceUniqueId;
	}

	public String getXmppUsername() {
		return xmppUsername;
	}

	public void setXmppUsername(String xmppUsername) {
		this.xmppUsername = xmppUsername;
	}

	public String getXmppPassword() {
		return xmppPassword;
	}

	public void setXmppPassword(String xmppPassword) {
		this.xmppPassword = xmppPassword;
	}

}
