package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderSyncResponseDto extends BaseSyncResponseDto {

    private String title;

    @JsonProperty("amount_paid")
    private BigDecimal amountPaid;

    private BigDecimal discount;

    private OrderStatus status;
}
