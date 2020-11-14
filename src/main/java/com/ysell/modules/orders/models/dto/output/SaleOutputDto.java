package com.ysell.modules.orders.models.dto.output;

import java.math.BigDecimal;
import java.util.UUID;

import com.ysell.modules.common.models.LookupDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleOutputDto {

	private UUID id;
	
	private LookupDto product;
	
    private int quantity;
    
    private double percentageDiscount;
    
    private BigDecimal totalPrice; 
}
