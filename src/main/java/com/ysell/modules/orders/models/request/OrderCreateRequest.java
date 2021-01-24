package com.ysell.modules.orders.models.request;

import com.ysell.modules.common.dto.LookupDto;
import com.ysell.modules.orders.models.dto.SaleCreateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@Getter
public class OrderCreateRequest {

    @NotNull
    @Valid
	private Set<SaleCreateDto> sales;

    @NotNull
	@Valid
    private LookupDto organisation;

	@NotNull
    private BigDecimal discount;

    @NotNull
    private BigDecimal amountPaid;
}
