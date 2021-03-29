package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.ProductEntity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductSyncResponseDto extends  BaseSyncResponseDto {

    private String name;

    @JsonProperty("price")
    private BigDecimal sellingPrice;

    @JsonProperty("cost")
    private BigDecimal costPrice;


    public static ProductSyncResponseDto from(ProductEntity productEntity) {
        ProductSyncResponseDto responseDto = new ProductSyncResponseDto();
        responseDto.setBaseFields(productEntity, productEntity.getOrganisation().getId());

        responseDto.name = productEntity.getName();
        responseDto.sellingPrice = productEntity.getSellingPrice();
        responseDto.costPrice = productEntity.getCostPrice();

        return responseDto;
    }
}
