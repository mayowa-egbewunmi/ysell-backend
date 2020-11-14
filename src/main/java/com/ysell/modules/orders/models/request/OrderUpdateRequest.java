package com.ysell.modules.orders.models.request;

import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.orders.models.dto.others.SaleUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderUpdateRequest {

	@NotNull
	private UUID id;
	
	private Set<SaleUpdateRequestDto> sales = new HashSet<>();

	@Valid
    private LookupDto organisation;
    
    private double percentageDiscount;
    
    private BigDecimal totalPrice;
}
