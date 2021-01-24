package com.ysell.modules.synchronisation.models.dto;

import com.ysell.jpa.entities.enums.OrderStatus;
import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class OrderResponseDto {

	private UUID id;
	
	private Set<SaleResponseDto> sales;
	
    private LookupDto organisation;
    
    private double percentageDiscount;
	
	private BigDecimal totalPrice;

    private OrderStatus status;


    @AllArgsConstructor
    @Getter
    public static class SaleResponseDto {

        private UUID id;

        private LookupDto product;

        private int quantity;

        private double percentageDiscount;

        private BigDecimal totalPrice;
    }
}
