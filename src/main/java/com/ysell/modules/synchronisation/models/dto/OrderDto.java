package com.ysell.modules.synchronisation.models.dto;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class OrderDto {

	@NotNull
	private UUID id;

	@Valid
	private LookupDto organisation;

	@Valid
	private Set<SaleDto> sales;

	@NotNull
	private BigDecimal discount;

	@NotNull
	private BigDecimal totalPrice;

	@NotNull
	private Integer status;

	@NotNull
	private LocalDate clientCreatedAt;

	private LocalDate clientUpdatedAt;


	@AllArgsConstructor
	@Getter
	public static class SaleDto {

		@NotNull
		private UUID id;

		@Valid
		private LookupDto product;

		@NotNull
		private Integer quantity;

		@NotNull
		private BigDecimal discount;

		@NotNull
		private BigDecimal totalPrice;

		@NotNull
		private LocalDate clientCreatedAt;

		private LocalDate clientUpdatedAt;
	}
}
