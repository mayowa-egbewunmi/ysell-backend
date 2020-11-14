package com.ysell.modules.orders.models.dto.others;

import java.math.BigDecimal;

import com.ysell.modules.common.models.LookupDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleRequestDto {
	
	private LookupDto product;
	
    private int quantity;
    
    private double percentageDiscount;
    
    private BigDecimal totalPrice; 
}
