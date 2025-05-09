package com.eai.user.entities;

public enum CustomerType {
	CREATED(0), ACTIVE(1), BLOCKED(2);

	private int type;

	CustomerType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
}
