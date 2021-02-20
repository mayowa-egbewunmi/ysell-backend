package com.ysell.modules.payment;

import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.payment.domain.PaymentService;
import com.ysell.modules.payment.models.request.PaymentRequest;
import com.ysell.modules.payment.models.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
@RestController
@RequestMapping(PaymentController.PATH)
@RequiredArgsConstructor
public class PaymentController {

    public static final String PATH = ControllerConstants.VERSION_URL + "/payments";

    private final PaymentService paymentService;


    @PostMapping
    public PaymentResponse makePayment(@RequestBody @Valid PaymentRequest request) {
        return paymentService.makePayment(request);
    }
}
