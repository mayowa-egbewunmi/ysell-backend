package com.ysell.modules.orders.models.request;

import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.orders.models.dto.others.SaleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequest {

    @Valid
	private Set<SaleRequestDto> sales = new HashSet<>();

	@Valid
    private LookupDto organisation;

    private Double percentageDiscount;
    
    private BigDecimal totalPrice;
}
