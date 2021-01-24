package com.ysell.modules.orders.models.dto;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class SaleCreateDto {

    @NotNull
    @Valid
    private LookupDto product;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal totalPrice; 
}
