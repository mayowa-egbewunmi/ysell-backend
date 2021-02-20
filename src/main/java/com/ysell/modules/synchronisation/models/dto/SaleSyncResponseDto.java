package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.SaleEntity;
import com.ysell.modules.common.utilities.MapperUtils;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class SaleSyncResponseDto extends BaseSyncResponseDto {

	@NotNull
	@JsonProperty("order_id")
	private UUID orderId;

	@NotNull
	@JsonProperty("product_id")
	private UUID productId;

	@NotNull
	private Long quantity;

	@NotNull
	@JsonProperty("total_price")
	private BigDecimal totalPrice;


	public static SaleSyncResponseDto from(SaleEntity saleEntity) {
		SaleSyncResponseDto responseDto = MapperUtils.allArgsMap(saleEntity, SaleSyncResponseDto.class);
		responseDto.orderId = saleEntity.getOrder().getId();
		responseDto.productId = saleEntity.getProduct().getId();
		responseDto.setOrganisationId(saleEntity.getOrder().getOrganisation().getId());
		responseDto.setDeleted(!saleEntity.isActive());
		return responseDto;
	}
}