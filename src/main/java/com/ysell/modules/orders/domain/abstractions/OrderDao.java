package com.ysell.modules.orders.domain.abstractions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ysell.modules.orders.enums.OrderStatus;
import com.ysell.modules.orders.models.dto.output.ProductDto;
import com.ysell.modules.orders.models.request.OrderRequest;
import com.ysell.modules.orders.models.request.OrderUpdateRequest;
import com.ysell.modules.orders.models.response.OrderResponse;

public interface OrderDao {

	Optional<ProductDto> getProduct(long productId);
	
	OrderResponse saveOrder(OrderRequest request, int status);
	
	OrderResponse updateOrder(OrderUpdateRequest request, int status);
	
	ProductDto updateProductStock(long productId, int quantity);
	
	boolean hasOrganisation(long organisationId);
	
	Optional<OrderResponse> getOrder(UUID id);
	
	OrderResponse updateOrderStatus(UUID id, OrderStatus status);
	
	List<OrderResponse> getOrderByOrganisation(long organisationId);
}
