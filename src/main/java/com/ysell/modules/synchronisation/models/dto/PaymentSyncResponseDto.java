package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.PaymentEntity;
import com.ysell.jpa.entities.enums.PaymentMode;
import com.ysell.modules.common.utilities.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class PaymentSyncResponseDto extends BaseSyncResponseDto {

	@JsonProperty("order_id")
	private UUID orderId;

	private PaymentMode mode;

	private BigDecimal amountPaid;


	public static PaymentSyncResponseDto from(PaymentEntity paymentEntity) {
		PaymentSyncResponseDto responseDto = MapperUtils.allArgsMap(paymentEntity, PaymentSyncResponseDto.class);
		responseDto.orderId = paymentEntity.getOrder().getId();
		responseDto.setOrganisationId(paymentEntity.getOrder().getOrganisation().getId());
		responseDto.setDeleted(!paymentEntity.isActive());
		return responseDto;
	}
}