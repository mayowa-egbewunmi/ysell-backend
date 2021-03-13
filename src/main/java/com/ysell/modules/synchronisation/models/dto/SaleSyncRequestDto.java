package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class SaleSyncRequestDto extends BaseSyncRequestDto {

	@NotNull
	@JsonProperty("order_id")
	private UUID orderId;

	@NotNull
	@JsonProperty("product_id")
	private UUID productId;

	@NotNull
	private Integer quantity;

	@NotNull
	@JsonProperty("total_price")
	private BigDecimal totalSellingPrice;

	@NotNull
	@JsonProperty("total_cost")
	private BigDecimal totalCostPrice;
}
