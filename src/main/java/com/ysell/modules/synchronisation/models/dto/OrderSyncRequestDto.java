package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class OrderSyncRequestDto extends BaseSyncRequestDto {

	@NotEmpty
	private String title;

	@NotNull
	@JsonProperty("amount_paid")
	private BigDecimal amountPaid;

	@NotNull
	private BigDecimal discount;

	@NotNull
	private OrderStatus status;
}
