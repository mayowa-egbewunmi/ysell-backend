package com.ysell.modules.payment.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.OrderEntity;
import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
@AllArgsConstructor
@Getter
public class PaymentResponse {

    private LookupDto order;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("amount_paid")
    private BigDecimal amountPaid;

    private String depositor;

    @JsonProperty("total_amount_paid")
    private BigDecimal totalAmountPaid;

    private BigDecimal amountDue;


    public static PaymentResponse from(OrderEntity orderEntity, BigDecimal amountPaid, String depositor) {
        return new PaymentResponse(
                LookupDto.create(orderEntity.getId(), orderEntity.getTitle()),
                orderEntity.getTotalPrice(),
                amountPaid,
                depositor,
                orderEntity.getAmountPaid(),
                orderEntity.getAmountDue()
        );
    }
}
