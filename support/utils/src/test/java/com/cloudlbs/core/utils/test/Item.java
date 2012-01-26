package com.cloudlbs.core.utils.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cloudlbs.core.utils.domain.DatabaseEntity;

/**
 * @author Dan Mascenik
 * 
 */
public class Item extends DatabaseEntity {

	private static final long serialVersionUID = -2456002186895184966L;

	private Date modDate;
	private Item parent;
	private String reqString;
	private Integer anInt;
	private Collection<String> labels = new ArrayList<String>();
	private Set<Item> childs = new HashSet<Item>();
	private Thing thing;
	private Date strDate;

	/**
	 * 
	 */
	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public Item(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public Item getParent() {
		return parent;
	}

	public void setParent(Item parent) {
		this.parent = parent;
	}

	public String getReqString() {
		return reqString;
	}

	public void setReqString(String reqString) {
		this.reqString = reqString;
	}

	public Integer getAnInt() {
		return anInt;
	}

	public void setAnInt(Integer anInt) {
		this.anInt = anInt;
	}

	public Collection<String> getLabels() {
		return labels;
	}

	public Thing getThing() {
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	public Date getStrDate() {
		return strDate;
	}

	public void setStrDate(Date strDate) {
		this.strDate = strDate;
	}

	public Set<Item> getChilds() {
		return childs;
	}

}
