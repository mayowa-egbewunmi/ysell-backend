package com.ysell.modules.orders.models.response;

import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.orders.enums.OrderStatus;
import com.ysell.modules.orders.models.dto.output.SaleOutputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponse {

	private UUID id;
	
	private Set<SaleOutputDto> sales;

    private LookupDto organisation;
    
    private double percentageDiscount;

    private OrderStatus status;
	
	private BigDecimal totalPrice;
}
