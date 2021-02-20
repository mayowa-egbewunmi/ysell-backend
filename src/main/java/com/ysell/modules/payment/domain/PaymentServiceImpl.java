package com.ysell.modules.payment.domain;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.PaymentEntity;
import com.ysell.jpa.repositories.OrderRepository;
import com.ysell.jpa.repositories.PaymentRepository;
import com.ysell.modules.common.dto.LookupDto;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.services.LoggedInUserService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.payment.models.request.PaymentRequest;
import com.ysell.modules.payment.models.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        OrderEntity orderEntity = tryGetOrder(request.getOrderId());
        if(!request.isTotalPaidIgnored() && orderEntity.paymentComplete())
            throw new YSellRuntimeException("Payment already completed for order");

        PaymentEntity paymentEntity = createPayment(request, orderEntity);
        orderEntity = updateOrder(orderEntity, paymentEntity);

        return PaymentResponse.builder()
                .order(LookupDto.create(orderEntity.getId(), orderEntity.getTitle()))
                .totalPrice(orderEntity.getTotalPrice())
                .amountPaid(request.getAmountPaid())
                .depositor(loggedInUserService.getLoggedInUser().getName())
                .totalAmountPaid(orderEntity.getAmountPaid())
                .amountDue(orderEntity.getAmountDue())
                .build();
    }


    private OrderEntity updateOrder(OrderEntity orderEntity, PaymentEntity paymentEntity) {
        orderEntity.getPayments().add(paymentEntity);
        return orderRepository.save(orderEntity);
    }


    private OrderEntity tryGetOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> ServiceUtils.wrongIdException("Order", orderId));
    }


    private PaymentEntity createPayment(PaymentRequest request, OrderEntity orderEntity) {
        PaymentEntity paymentEntity = new PaymentEntity(
                orderEntity, request.getAmountPaid(), request.getMode(), request.getNarration()
        );

        return paymentRepository.save(paymentEntity);
    }
}
