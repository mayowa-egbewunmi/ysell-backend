package com.ysell.modules.product.models.request;

import com.ysell.modules.common.models.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateProductRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal price;

    @Valid
    private LookupDto organisation;

    @Valid
    private LookupDto productCategory;   		
}
