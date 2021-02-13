package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.enums.PaymentMode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PaymentSyncResponseDto extends BaseSyncResponseDto {

	@JsonProperty("order_id")
	private UUID orderId;

	private PaymentMode mode;

	private BigDecimal amount;
}
