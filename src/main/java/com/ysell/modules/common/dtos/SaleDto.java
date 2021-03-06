package com.ysell.modules.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.enums.SaleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SaleDto {

	private UUID id;
	
	private LookupDto product;
	
    private int quantity;

    @JsonProperty("total_price")
    private BigDecimal totalSellingPrice;

    @JsonProperty("total_cost")
    private BigDecimal totalCostPrice;

    private SaleType saleType;
}
