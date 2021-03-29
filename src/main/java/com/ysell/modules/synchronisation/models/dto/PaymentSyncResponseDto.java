package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.PaymentEntity;
import com.ysell.jpa.entities.enums.PaymentMode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class PaymentSyncResponseDto extends BaseSyncResponseDto {

	@JsonProperty("order_id")
	private UUID orderId;

	private PaymentMode mode;

	private BigDecimal amountPaid;


	public static PaymentSyncResponseDto from(PaymentEntity paymentEntity) {
		PaymentSyncResponseDto responseDto = new PaymentSyncResponseDto();
		responseDto.setBaseFields(paymentEntity, paymentEntity.getOrder().getOrganisation().getId());

		responseDto.orderId = paymentEntity.getOrder().getId();
		responseDto.mode = paymentEntity.getMode();
		responseDto.amountPaid = paymentEntity.getAmount();

		return responseDto;
	}
}