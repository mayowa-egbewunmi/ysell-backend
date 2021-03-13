package com.ysell.modules.orders.models.request;

import com.ysell.modules.common.dtos.LookupDto;
import com.ysell.modules.orders.models.dto.SaleCreateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderCreateRequest {

    @NotNull
    private String title;

    @NotNull
    @Valid
	private Set<SaleCreateDto> sales;

    @NotNull
	@Valid
    private LookupDto organisation;
}
