package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductSyncRequestDto extends BaseSyncRequestDto {

    @NotNull
    private String name;

    @NotNull
    @JsonProperty("price")
    private BigDecimal sellingPrice;

    @NotNull
    @JsonProperty("cost")
    private BigDecimal costPrice;
}
