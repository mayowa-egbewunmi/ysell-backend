package com.ysell.modules.product.models.request;

import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProductRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal costPrice;

    @NotNull
    private BigDecimal sellingPrice;

    @NotNull
    @Valid
    private LookupDto organisation;

    @NotNull
    @Valid
    private LookupDto productCategory;   		
}
