package com.ysell.modules.payment.domain;

import com.ysell.modules.payment.models.request.PaymentRequest;
import com.ysell.modules.payment.models.response.PaymentResponse;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest request);
}
