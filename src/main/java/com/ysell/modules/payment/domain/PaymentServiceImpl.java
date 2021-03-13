package com.ysell.modules.payment.domain;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.PaymentEntity;
import com.ysell.jpa.repositories.OrderRepository;
import com.ysell.jpa.repositories.PaymentRepository;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.services.LoggedInUserService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.payment.models.request.PaymentRequest;
import com.ysell.modules.payment.models.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    private final LoggedInUserService loggedInUserService;


    @Override
    public PaymentResponse makePayment(PaymentRequest request) {
        OrderEntity orderEntity = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> ServiceUtils.wrongIdException("Order", request.getOrderId()));

        if (!request.getTotalPaidIgnored() && orderEntity.paymentComplete())
            throw new YSellRuntimeException("Payment already completed for order");

        createPayment(request, orderEntity);

        orderEntity = orderRepository.getOne(request.getOrderId());

        return PaymentResponse.from(orderEntity, request.getAmountPaid(), loggedInUserService.getLoggedInUser().getName());
    }


    private void createPayment(PaymentRequest request, OrderEntity orderEntity) {
        PaymentEntity paymentEntity = new PaymentEntity(
                orderEntity,
                request.getAmountPaid(),
                request.getMode(),
                request.getNarration()
        );

        paymentRepository.save(paymentEntity);
    }
}