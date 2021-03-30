package com.ysell.modules.product.models.request;

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
public class ProductRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    @JsonProperty("cost_price")
    private BigDecimal costPrice;

    @NotNull
    @JsonProperty("selling_price")
    private BigDecimal sellingPrice;

    @NotNull
    @Valid
    private LookupDto organisation;

    @NotNull
    @Valid
    @JsonProperty("product_category")
    private LookupDto productCategory;   		
}
