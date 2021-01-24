package com.ysell.modules.orders.models.response;

import com.ysell.jpa.entities.enums.OrderStatus;
import com.ysell.modules.common.dto.LookupDto;
import com.ysell.modules.orders.models.dto.SaleOutputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class OrderResponse {

	private UUID id;

    private LookupDto organisation;
	
	private Set<SaleOutputDto> sales;
    
    private BigDecimal discount;
	
	private BigDecimal amountPaid;

    private OrderStatus status;
}
