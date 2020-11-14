package com.ysell.modules.synchronisation.models.dto.output;

import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.orders.enums.OrderStatus;
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
public class OrderResponseDto {

	private UUID id;
	
	private Set<SaleResponseDto> sales;
	
    private LookupDto organisation;
    
    private double percentageDiscount;
	
	private BigDecimal totalPrice;

    private OrderStatus status;
}
