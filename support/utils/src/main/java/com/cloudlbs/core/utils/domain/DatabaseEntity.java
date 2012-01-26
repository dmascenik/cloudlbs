package com.cloudlbs.core.utils.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Version;

/**
 * All database entities are expected to implement a primary key ID that extends
 * {@link Serializable}, as well as a String GUID field. Both of these fields
 * are expected to be immutable. The same goes for <code>createDate</code>.
 * 
 * @author Dan Mascenik
 * 
 */
@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DatabaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private Long id;

	@Column(name = "GUID", unique = true, nullable = false)
	private String guid;

	@Column(name = "CREATE_DATE", nullable = false)
	private Date createDate = new Date();

	@Version
	@Column(name = "OPTLOCK")
	private Long version;

	public DatabaseEntity() {
	}

	public DatabaseEntity(String guid) {
		this.guid = guid;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public String getGuid() {
		return guid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @deprecated IMMUTABLE - method does nothing!
	 * @param id
	 */
	public final void setId(Long id) {
		// Immutable - do nothing
	}

	/**
	 * @deprecated IMMUTABLE - method does nothing!
	 * @param guid
	 */
	public final void setGuid(String guid) {
		// Immutable - do nothing
	}

	/**
	 * @deprecated IMMUTABLE - method does nothing!
	 * @param createDate
	 */
	public final void setCreateDate(Date createDate) {
		// Immutable - do nothing
	}

	/**
	 * This is based exclusively on the GUID. This means that
	 * {@link DatabaseEntity}s that have not yet been persisted (GUID is null)
	 * cannot be hashed.
	 */
	@Override
	public int hashCode() {
		if (guid == null) {
			return super.hashCode();
		} else {
			return guid.hashCode();
		}
	}

	/**
	 * This is based exclusively on the GUID. This means that
	 * {@link DatabaseEntity}s that have not yet been persisted (GUID is null)
	 * cannot be compared.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!DatabaseEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		} else if (guid == null) {
			return super.equals(obj);
		} else {
			return guid.equals(((DatabaseEntity) obj).getGuid());
		}
	}

}
