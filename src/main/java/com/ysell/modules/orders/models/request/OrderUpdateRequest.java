package com.ysell.modules.orders.models.request;

import com.ysell.modules.common.dtos.LookupDto;
import com.ysell.modules.orders.models.dto.SaleUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderUpdateRequest {

    @NotNull
    private String title;

    @NotNull
    @Valid
    private Set<SaleUpdateDto> sales = new HashSet<>();

    @NotNull
	@Valid
    private LookupDto organisation;
}
