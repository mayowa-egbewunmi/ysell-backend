package com.ysell.modules.orders.domain.abstractions;

import com.ysell.modules.orders.models.request.OrderByOrganisationRequest;
import com.ysell.modules.orders.models.request.OrderIdRequest;
import com.ysell.modules.orders.models.request.OrderRequest;
import com.ysell.modules.orders.models.request.OrderUpdateRequest;
import com.ysell.modules.orders.models.response.OrderResponse;

import java.util.List;

public interface OrderService {

	OrderResponse postOrder(OrderRequest request);

	List<OrderResponse> getOrdersByOrganisation(OrderByOrganisationRequest request);
	
	OrderResponse updateOrder(OrderUpdateRequest request);
	
	OrderResponse approveOrder(OrderIdRequest request);

	OrderResponse cancelOrder(OrderIdRequest request);
}
