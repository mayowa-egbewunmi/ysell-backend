package com.ysell.modules.orders.domain;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.SaleEntity;
import com.ysell.jpa.entities.enums.OrderStatus;
import com.ysell.jpa.repositories.OrderRepository;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.jpa.repositories.SaleRepository;
import com.ysell.modules.common.services.ProductStockService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.orders.models.request.OrderCreateRequest;
import com.ysell.modules.orders.models.request.OrderUpdateRequest;
import com.ysell.modules.orders.models.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepo;

	private final SaleRepository saleRepo;

	private final ProductRepository productRepo;

	private final OrganisationRepository orgRepo;

	private final ProductStockService productStockService;

	private final ModelMapper mapper = new ModelMapper();


	@Override
	public OrderResponse postOrder(OrderCreateRequest request) {
		request.getSales().forEach(sale -> productStockService.validateProductAndStock(
				request.getOrganisation().getId(),
				sale.getProduct().getId(),
				sale.getQuantity())
		);

		request.getSales().forEach(sale ->
				deductFromProductStock(sale.getProduct().getId(), sale.getQuantity()));

		OrderEntity orderEntity = mapper.map(request, OrderEntity.class);
		orderEntity.setStatus(OrderStatus.PENDING);

		return saveOrUpdateOrder(orderEntity);
	}


	@Override
	public OrderResponse updateOrder(UUID orderId, OrderUpdateRequest request) {
		OrderEntity orderEntity = orderRepo.findById(orderId)
				.orElseThrow(() -> ServiceUtils.wrongIdException("Order", orderId));

		request.getSales().forEach(sale -> {
			if (!saleRepo.existsById(sale.getId()))
				ServiceUtils.throwWrongIdException("Sale", sale.getId());

			productStockService.validateProductAndStock(
					request.getOrganisation().getId(),
					sale.getProduct().getId(),
					sale.getQuantity());
		});

		addPreviousOrderBackToProductStock(orderEntity.getSales());

		request.getSales().forEach(sale ->
				deductFromProductStock(sale.getProduct().getId(), sale.getQuantity()));

		mapper.map(request, orderEntity);

		return saveOrUpdateOrder(orderEntity);
	}


	@Override
	public OrderResponse approveOrder(UUID orderId) {
		return updateOrderStatus(orderId, OrderStatus.OPEN);
	}


	@Override
	public OrderResponse cancelOrder(UUID orderId) {
		return updateOrderStatus(orderId, OrderStatus.CANCELLED);
	}


	@Override
	public List<OrderResponse> getOrdersByOrganisationIds(Set<UUID> organisationIds) {
		organisationIds.forEach(organisationId -> {
			if (!orgRepo.existsById(organisationId))
				throw ServiceUtils.wrongIdException("Order", organisationId);
		});

		List<OrderEntity> orderEntities = orderRepo.findByOrganisationIdIn(organisationIds);

		return orderEntities.stream()
				.map(entity -> mapper.map(entity, OrderResponse.class))
				.collect(Collectors.toList());
	}


	private void deductFromProductStock(UUID productId, int quantityUsed) {
		int quantityToRemove = quantityUsed * -1;
		productStockService.updateProductStock(productId, quantityToRemove);
	}


	private void addPreviousOrderBackToProductStock(Set<SaleEntity> saleEntities) {
		saleEntities.forEach(sale -> productStockService.updateProductStock(sale.getProduct().getId(), sale.getQuantity()));
	}


	private OrderResponse saveOrUpdateOrder(OrderEntity entity) {
		entity.getSales().forEach(sale -> sale.setOrder(entity));
		OrderEntity order = orderRepo.save(entity);

		OrderResponse response = mapper.map(entity, OrderResponse.class);

		return enrichResponseWithNames(response);
	}


	private OrderResponse updateOrderStatus(UUID orderId, OrderStatus status) {
		OrderEntity entity = orderRepo.findById(orderId)
				.orElseThrow(() -> ServiceUtils.wrongIdException("Order", orderId));
		entity.setStatus(status);
		entity = orderRepo.save(entity);

		OrderResponse response = mapper.map(entity, OrderResponse.class);

		return enrichResponseWithNames(response);
	}


	private OrderResponse enrichResponseWithNames(OrderResponse response) {
		String orgName = orgRepo.getOne(response.getOrganisation().getId()).getName();
		response.getOrganisation().setName(orgName);

		response.getSales().forEach(sale -> {
			String productName = productRepo.getOne(sale.getProduct().getId()).getName();
			sale.getProduct().setName(productName);
		});

		return response;
	}
}
