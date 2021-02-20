package com.ysell.modules.synchronisation.models.dto;

import com.ysell.jpa.entities.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderSyncRequestDto extends BaseSyncRequestDto {

	@NotEmpty
	private String title;

	@NotNull
	private OrderStatus status;
}
