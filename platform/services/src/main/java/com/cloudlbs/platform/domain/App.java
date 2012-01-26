package com.cloudlbs.platform.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudlbs.core.utils.domain.DatabaseEntity;

/**
 * An {@link App} is a thing that runs on a {@link Device}. It may be installed
 * on many different devices and used by many different users.<br/>
 * <br/>
 * NOTE: This is the <i>inverse</i> end of the many-to-many relationship with
 * {@link Device}s and {@link UserAccount}s, so the {@link #userAccounts} and
 * {@link #devices} sets will not be populated on load of this entity. Likewise,
 * changes to UserAccounts or Devices from this entity will not be persisted.
 * 
 * @author Dan Mascenik
 * 
 */
@Entity
@Table(name = "APP")
public class App extends DatabaseEntity {

	private static final long serialVersionUID = 3658245555333630235L;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "USES_USER_ACCOUNTS", nullable = false)
	private Boolean usesUserAccounts = false;

	@Column(name = "ANDROID_PACKAGE_NAME")
	private String androidPackageName;

	@Column(name = "IOS_BUNDLE_ID")
	private String iosBundleId;

	// /**
	// * An {@link App} may be associated with N {@link UserAccount}s
	// */
	// @ManyToMany(mappedBy = "apps")
	// @SuppressWarnings("unused")
	// private Set<UserAccount> userAccounts = new HashSet<UserAccount>();

	// /**
	// * An {@link App} may be associated with N {@link Device}s
	// */
	// @ManyToMany(mappedBy = "apps")
	// @SuppressWarnings("unused")
	// private Set<Device> devices = new HashSet<Device>();

	public App() {
		super();
	}

	public App(String guid) {
		super(guid);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getUsesUserAccounts() {
		return usesUserAccounts;
	}

	public void setUsesUserAccounts(Boolean usesUserAccounts) {
		this.usesUserAccounts = usesUserAccounts;
	}

	public String getAndroidPackageName() {
		return androidPackageName;
	}

	public void setAndroidPackageName(String androidPackageName) {
		this.androidPackageName = androidPackageName;
	}

	public String getIosBundleId() {
		return iosBundleId;
	}

	public void setIosBundleId(String iosBundleId) {
		this.iosBundleId = iosBundleId;
	}

}
