package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.enums.PaymentMode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PaymentSyncRequestDto extends BaseSyncRequestDto {

	@NotNull
	@JsonProperty("order_id")
	private UUID orderId;

	@NotNull
	private PaymentMode mode;

	@NotNull
	private BigDecimal amount;
}
