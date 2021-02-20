package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.ProductEntity;
import com.ysell.modules.common.utilities.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProductSyncResponseDto extends  BaseSyncResponseDto {

    private String name;

    @JsonProperty("price")
    private BigDecimal sellingPrice;

    @JsonProperty("cost")
    private BigDecimal costPrice;


    public static ProductSyncResponseDto from(ProductEntity productEntity) {
        ProductSyncResponseDto responseDto = MapperUtils.allArgsMap(productEntity, ProductSyncResponseDto.class);
        responseDto.setOrganisationId(productEntity.getOrganisation().getId());
        responseDto.setDeleted(!productEntity.isActive());
        return responseDto;
    }
}
