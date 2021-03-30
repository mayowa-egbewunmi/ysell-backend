package com.ysell.modules.payment.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentRequest {

    @NotNull
    @JsonProperty("order_id")
    private UUID orderId;

    @NotNull
    @JsonProperty("amount_paid")
    private BigDecimal amountPaid;

    @NotNull
    private PaymentMode mode;

    private String narration;

    @JsonProperty("ignore_total_paid")
    private Boolean totalPaidIgnored;
}
