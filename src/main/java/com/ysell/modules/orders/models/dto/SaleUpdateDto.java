package com.ysell.modules.orders.models.dto;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class SaleUpdateDto {

	@NotNull
	private UUID id;

	@NotNull
	@Valid
	private LookupDto product;

	@NotNull
	private Integer quantity;

	@NotNull
	private BigDecimal totalPrice;
}
