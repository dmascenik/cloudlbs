package com.cloudlbs.core.utils.search;

import java.io.Serializable;

public class Sort implements Serializable {

	private static final long serialVersionUID = -1493189068684868392L;

	public enum Direction implements Serializable {
		Ascending, Descending
	}

	private String field;

	private Direction direction;

	public Sort(String field, Direction direction) {
		this.field = field;
		this.direction = direction;
	}

	public String getField() {
		return field;
	}

	public Direction getDirection() {
		return direction;
	}

}
