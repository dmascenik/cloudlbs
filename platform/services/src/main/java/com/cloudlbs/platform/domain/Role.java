package com.cloudlbs.platform.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.cloudlbs.core.utils.domain.DatabaseEntity;

/**
 * A {@link Role} is the persistent entity equivalent of a Spring Security
 * {@link GrantedAuthority}.
 * 
 * @author Dan Mascenik
 * 
 */
@Entity
@Table(name = "SECURITY_ROLE")
public class Role extends DatabaseEntity {

	private static final long serialVersionUID = -1206120505399894305L;

	@Column(name = "NAME", unique = true, nullable = false)
	private String name;

	public Role() {
		super();
	}

	public Role(String guid) {
		super(guid);
	}

	public Role(String guid, String name) {
		super(guid);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
