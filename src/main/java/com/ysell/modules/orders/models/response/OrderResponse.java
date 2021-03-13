package com.ysell.modules.orders.models.response;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.enums.OrderStatus;
import com.ysell.modules.common.dtos.LookupDto;
import com.ysell.modules.common.dtos.PaymentDto;
import com.ysell.modules.common.dtos.SaleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class OrderResponse {

    private UUID id;

    private LookupDto organisation;

    private Set<SaleDto> sales;

    private OrderStatus status;

    private Set<PaymentDto> payments;


    public static OrderResponse from(OrderEntity orderEntity) {
        return new OrderResponse(
                orderEntity.getId(),
                LookupDto.create(orderEntity.getOrganisation()),
                orderEntity.getSales().stream().map(saleEntity -> new SaleDto(
                        saleEntity.getId(),
                        LookupDto.create(saleEntity.getProduct()),
                        saleEntity.getQuantity(),
                        saleEntity.getTotalSellingPrice(),
                        saleEntity.getTotalCostPrice(),
                        saleEntity.getSaleType()
                )).collect(Collectors.toSet()),
                orderEntity.getStatus(),
                orderEntity.getPayments().stream().map(paymentEntity -> new PaymentDto(
                        paymentEntity.getId(),
                        null,
                        paymentEntity.getAmount(),
                        paymentEntity.getMode(),
                        paymentEntity.getNarration()
                )).collect(Collectors.toSet())
        );
    }
}
