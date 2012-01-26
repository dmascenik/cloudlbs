package com.cloudlbs.core.utils.test;

import java.util.Date;

import com.cloudlbs.core.utils.domain.DatabaseEntity;

/**
 * @author Dan Mascenik
 * 
 */
public class Thing extends DatabaseEntity {

	private static final long serialVersionUID = -3618856089009738051L;

	private Date modDate;

	/**
	 * 
	 */
	public Thing() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public Thing(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

}
