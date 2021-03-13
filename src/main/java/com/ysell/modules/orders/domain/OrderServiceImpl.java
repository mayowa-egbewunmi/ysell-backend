package com.ysell.modules.orders.domain;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.SaleEntity;
import com.ysell.jpa.entities.enums.OrderStatus;
import com.ysell.jpa.repositories.OrderRepository;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.services.ProductStockService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.orders.models.request.OrderCreateRequest;
import com.ysell.modules.orders.models.request.OrderUpdateRequest;
import com.ysell.modules.orders.models.response.OrderResponse;
import com.ysell.modules.orders.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
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

	private final ProductRepository productRepo;

	private final OrganisationRepository orgRepo;

	private final ProductStockService productStockService;


	@Override
	public OrderResponse postOrder(OrderCreateRequest request) {
		request.getSales().forEach(sale -> productStockService.validateProductAndStock(
				request.getOrganisation().getId(),
				sale.getProduct().getId(),
				sale.getQuantity())
		);

		request.getSales().forEach(sale -> productStockService.updateProductStock(
				sale.getProduct().getId(),
				sale.getQuantity())
		);

		OrderEntity orderEntity = buildOrderEntity(request);

		return saveOrUpdateOrder(orderEntity);
	}


	@Override
	public OrderResponse updateOrder(UUID orderId, OrderUpdateRequest request) {
		OrderEntity orderEntity = orderRepo.findById(orderId)
				.orElseThrow(() -> ServiceUtils.wrongIdException("Order", orderId));

		if(orderEntity.getOrganisation().getId() != request.getOrganisation().getId()) {
			throw new YSellRuntimeException(String.format(
					"Request's organisation id (%s) does not match the order's organisation id (%s)",
					orderEntity.getOrganisation().getId(),
					request.getOrganisation().getId()
			));
		}

		request.getSales().forEach(sale -> productStockService.validateProductAndStock(
				request.getOrganisation().getId(),
				sale.getProduct().getId(),
				sale.getQuantity() * OrderUtils.getIntValueBySaleType(sale.getSaleType())
		));

		request.getSales().forEach(sale -> productStockService.updateProductStock(
				sale.getProduct().getId(),
				sale.getQuantity() * OrderUtils.getIntValueBySaleType(sale.getSaleType())
		));

		OrderEntity updateOrderEntity = updateOrderEntity(orderEntity, request);

		return saveOrUpdateOrder(updateOrderEntity);
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
				throw ServiceUtils.wrongIdException("Organisation", organisationId);
		});

		List<OrderEntity> orderEntities = orderRepo.findByOrganisationIdIn(organisationIds);

		return orderEntities.stream()
				.map(OrderResponse::from)
				.collect(Collectors.toList());
	}


	private OrderEntity buildOrderEntity(OrderCreateRequest request) {
		return OrderEntity.builder()
				.title(request.getTitle())
				.sales(request.getSales().stream().map(saleCreateDto -> SaleEntity.builder()
						.product(productRepo.getOne(saleCreateDto.getProduct().getId()))
						.quantity(saleCreateDto.getQuantity())
						.totalSellingPrice(saleCreateDto.getTotalSellingPrice())
						.totalCostPrice(saleCreateDto.getTotalCostPrice())
						.build())
						.collect(Collectors.toSet()))
				.organisation(orgRepo.getOne(request.getOrganisation().getId()))
				.build();
	}


	private OrderEntity updateOrderEntity(OrderEntity orderEntity, OrderUpdateRequest request) {
		orderEntity.setTitle(request.getTitle());
		orderEntity.getSales().addAll(
				request.getSales().stream().map(saleCreateDto -> SaleEntity.builder()
						.product(productRepo.getOne(saleCreateDto.getProduct().getId()))
						.quantity(saleCreateDto.getQuantity())
						.totalSellingPrice(saleCreateDto.getTotalSellingPrice())
						.totalCostPrice(saleCreateDto.getTotalCostPrice())
						.saleType(saleCreateDto.getSaleType())
						.build())
						.collect(Collectors.toSet())
		);

		return orderEntity;
	}


	private OrderResponse saveOrUpdateOrder(OrderEntity entity) {
		entity.getSales().forEach(sale -> sale.setOrder(entity));
		OrderEntity orderEntity = orderRepo.save(entity);

		return OrderResponse.from(orderEntity);
	}


	private OrderResponse updateOrderStatus(UUID orderId, OrderStatus status) {
		OrderEntity entity = orderRepo.findById(orderId)
				.orElseThrow(() -> ServiceUtils.wrongIdException("Order", orderId));
		entity.setStatus(status);
		entity = orderRepo.save(entity);

		return OrderResponse.from(entity);
	}
}
