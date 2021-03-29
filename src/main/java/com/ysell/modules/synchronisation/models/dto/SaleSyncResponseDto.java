package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.SaleEntity;
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
	@JsonProperty("total_price")
	private BigDecimal totalPrice;


	public static SaleSyncResponseDto from(SaleEntity saleEntity) {
		SaleSyncResponseDto responseDto = new SaleSyncResponseDto();
		responseDto.setBaseFields(saleEntity, saleEntity.getOrder().getOrganisation().getId());

		responseDto.orderId = saleEntity.getOrder().getId();
		responseDto.productId = saleEntity.getProduct().getId();
		responseDto.quantity = saleEntity.getQuantity();
		responseDto.totalPrice = saleEntity.getTotalSellingPrice();

		return responseDto;
	}
}