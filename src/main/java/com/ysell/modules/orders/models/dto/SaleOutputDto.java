package com.ysell.modules.orders.models.dto;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class SaleOutputDto {

	private UUID id;
	
	private LookupDto product;
	
    private int quantity;
    
    private BigDecimal totalPrice; 
}
