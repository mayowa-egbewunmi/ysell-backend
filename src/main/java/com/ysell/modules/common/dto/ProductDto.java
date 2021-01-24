package com.ysell.modules.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
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