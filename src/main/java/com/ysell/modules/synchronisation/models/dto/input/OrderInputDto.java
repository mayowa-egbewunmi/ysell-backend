package com.ysell.modules.synchronisation.models.dto.input;

import com.ysell.modules.common.models.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderInputDto {

	@NotNull
	private UUID id;

	@Valid
	private LookupDto organisation;

	@Valid
	private Set<SaleInputDto> sales;

	private double percentageDiscount;

	private BigDecimal totalPrice;

	@NotNull
	private Integer status;

	@NotNull
	private Date clientCreatedAt;

	private Date clientUpdatedAt;
}
