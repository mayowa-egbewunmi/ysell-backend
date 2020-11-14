package com.ysell.modules.orders.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

	PENDING(1),
	
	APPROVED(2),
	
	CANCELLED(3);
	
	
	private final int value;
	
	private OrderStatus(int value) {
		this.value = value;
	}
	
	public static OrderStatus fromValue(int value) {
		return OrderStatus.values()[value - 1];
	}
}
