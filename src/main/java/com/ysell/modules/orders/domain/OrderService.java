package com.ysell.modules.orders.domain;

import com.ysell.modules.orders.models.request.OrderCreateRequest;
import com.ysell.modules.orders.models.request.OrderUpdateRequest;
import com.ysell.modules.orders.models.response.OrderResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderService {

	OrderResponse postOrder(OrderCreateRequest request);
	
	OrderResponse updateOrder(UUID orderId, OrderUpdateRequest request);
	
	OrderResponse approveOrder(UUID orderId);

	OrderResponse cancelOrder(UUID orderId);

	List<OrderResponse> getOrdersByOrganisationIds(Set<UUID> organisationIds);
}
