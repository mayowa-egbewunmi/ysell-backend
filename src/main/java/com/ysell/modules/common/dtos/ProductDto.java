package com.ysell.modules.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProductDto {

    private UUID id;

    private String name;

    private String description;

    private Integer currentStock;

    private BigDecimal costPrice;

    private BigDecimal sellingPrice;

    private LookupDto organisation;

    private LookupDto productCategory;
}