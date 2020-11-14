package com.ysell.jpa.entities.base;

public interface ActiveEntity {
	
	boolean isActive();
	
	void setActive(boolean active);
}