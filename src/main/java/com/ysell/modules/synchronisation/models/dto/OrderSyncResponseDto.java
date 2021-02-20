package com.ysell.modules.synchronisation.models.dto;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.enums.OrderStatus;
import com.ysell.modules.common.utilities.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderSyncResponseDto extends BaseSyncResponseDto {

    private String title;

    private OrderStatus status;


    public static OrderSyncResponseDto from(OrderEntity orderEntity) {
        OrderSyncResponseDto responseDto = MapperUtils.allArgsMap(orderEntity, OrderSyncResponseDto.class);
        responseDto.setOrganisationId(orderEntity.getOrganisation().getId());
        responseDto.setDeleted(!orderEntity.isActive());
        return responseDto;
    }
}