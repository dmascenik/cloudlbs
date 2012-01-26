package com.cloudlbs.platform.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cloudlbs.core.utils.domain.DatabaseEntity;

/**
 * 
 * @author Dan Mascenik
 * 
 */
@Entity
@Table(name = "API_KEY")
public class ApiKey extends DatabaseEntity {

	private static final long serialVersionUID = -1938521520099954614L;
	public static final String PLATFORM_ANDROID = "Android";
	public static final String PLATFORM_IOS = "iOS";

	@Column(name = "KEY_STRING", nullable = false)
	private String keyString;

	@Column(name = "CERT_FINGERPRINT", nullable = false)
	private String certFingerprint;

	@Column(name = "PLATFORM", nullable = false)
	private String platform;

	@Column(name = "SEED", nullable = false)
	private String seed;

	/**
	 * Default is one year from now
	 */
	@Column(name = "EXPIRATION_DATE", nullable = false)
	private Date expirationDate = new Date(System.currentTimeMillis()
			+ (long) (1000 * 60 * 60 * 24 * 365));

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "APP_ID", nullable = false)
	private App app;

	public ApiKey() {
		super();
	}

	public ApiKey(String guid) {
		super(guid);
	}

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}

	public String getCertFingerprint() {
		return certFingerprint;
	}

	public void setCertFingerprint(String certFingerprint) {
		this.certFingerprint = certFingerprint;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

}
