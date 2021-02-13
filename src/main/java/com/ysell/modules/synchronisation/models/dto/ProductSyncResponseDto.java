package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductSyncResponseDto extends  BaseSyncResponseDto {

    private String name;

    @JsonProperty("price")
    private BigDecimal sellingPrice;

    @JsonProperty("cost")
    private BigDecimal costPrice;
}
