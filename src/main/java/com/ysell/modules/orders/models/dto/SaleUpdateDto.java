package com.ysell.modules.orders.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.enums.SaleType;
import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaleUpdateDto {

	@NotNull
	private UUID id;

	@NotNull
	@Valid
	private LookupDto product;

	@NotNull
	private Integer quantity;

	@NotNull
	@JsonProperty("total_price")
	private BigDecimal totalSellingPrice;

	@JsonProperty("total_cost")
	private BigDecimal totalCostPrice;

	@NotNull
	private SaleType saleType;
}