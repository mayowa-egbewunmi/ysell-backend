package com.ysell.modules.synchronisation.models.dto;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.enums.OrderStatus;
import lombok.Getter;

@Getter
public class OrderSyncResponseDto extends BaseSyncResponseDto {

    private String title;

    private OrderStatus status;


    public static OrderSyncResponseDto from(OrderEntity orderEntity) {
        OrderSyncResponseDto responseDto = new OrderSyncResponseDto();
        responseDto.setBaseFields(orderEntity, orderEntity.getOrganisation().getId());

        responseDto.title = orderEntity.getTitle();
        responseDto.status = orderEntity.getStatus();

        return responseDto;
    }
}