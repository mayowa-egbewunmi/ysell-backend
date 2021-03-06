package com.ysell.modules.orders.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaleCreateDto {

    @NotNull
    @Valid
    private LookupDto product;

    @NotNull
    private Integer quantity;

    @NotNull
    @JsonProperty("total_price")
    private BigDecimal totalSellingPrice;

    @JsonProperty("total_cost")
    private BigDecimal totalCostPrice;
}
