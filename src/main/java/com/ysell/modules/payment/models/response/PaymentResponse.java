package com.ysell.modules.payment.models.response;

import com.ysell.modules.common.dto.LookupDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
@Builder
@Getter
public class PaymentResponse {

    private LookupDto order;

    private BigDecimal totalPrice;

    private BigDecimal amountPaid;

    private String depositor;

    private BigDecimal totalAmountPaid;

    private BigDecimal amountDue;
}
