package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.SaleEntity;
import com.ysell.jpa.entities.enums.SaleType;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class SaleSyncResponseDto extends BaseSyncResponseDto {

	@NotNull
	@JsonProperty("order_id")
	private UUID orderId;

	@NotNull
	@JsonProperty("product_id")
	private UUID productId;

	@NotNull
	private int quantity;

	@NotNull
	@JsonProperty("total_cost")
	private BigDecimal totalCost;

	@NotNull
	@JsonProperty("total_price")
	private BigDecimal totalPrice;

	@NotNull
	@JsonProperty("sale_type")
	private SaleType saleType;


	public static SaleSyncResponseDto from(SaleEntity saleEntity) {
		SaleSyncResponseDto responseDto = new SaleSyncResponseDto();
		responseDto.setBaseFields(saleEntity, saleEntity.getOrder().getOrganisation().getId());

		responseDto.orderId = saleEntity.getOrder().getId();
		responseDto.productId = saleEntity.getProduct().getId();
		responseDto.quantity = saleEntity.getQuantity();
		responseDto.totalCost = saleEntity.getTotalCostPrice();
		responseDto.totalPrice = saleEntity.getTotalSellingPrice();
		responseDto.saleType = saleEntity.getSaleType();

		return responseDto;
	}
}