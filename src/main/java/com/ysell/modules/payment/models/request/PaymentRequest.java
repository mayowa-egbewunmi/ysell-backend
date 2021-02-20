package com.ysell.modules.payment.models.request;

import com.ysell.jpa.entities.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
@AllArgsConstructor
@Getter
public class PaymentRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private BigDecimal amountPaid;

    @NotNull
    private PaymentMode mode;

    private String narration;

    private boolean totalPaidIgnored;
}
