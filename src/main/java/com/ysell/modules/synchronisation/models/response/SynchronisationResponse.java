package com.ysell.modules.synchronisation.models.response;

import com.ysell.modules.synchronisation.models.dto.OrderResponseDto;
import com.ysell.modules.synchronisation.models.dto.ProductResponseDto;
import com.ysell.modules.synchronisation.models.dto.StockResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

//todo: use camel case for json response
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SynchronisationResponse {

	private Set<ProductResponseDto> newProducts;

	private Set<ProductResponseDto> updatedProducts;

	private Set<OrderResponseDto> newOrders;

	private Set<OrderResponseDto> updatedOrders;

	private Set<StockResponseDto> newStocks;

	private LocalDate lastSyncTime;
}
