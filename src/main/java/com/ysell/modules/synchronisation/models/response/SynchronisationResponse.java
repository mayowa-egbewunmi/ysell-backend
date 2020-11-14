package com.ysell.modules.synchronisation.models.response;

import com.ysell.modules.synchronisation.models.dto.output.OrderResponseDto;
import com.ysell.modules.synchronisation.models.dto.output.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SynchronisationResponse {

	private Date lastSyncTime;

	private Set<OrderResponseDto> newOrders;	

	private Set<OrderResponseDto> updatedOrders;

	private Set<ProductResponseDto> newProducts;	

	private Set<ProductResponseDto> updatedProducts;
}
