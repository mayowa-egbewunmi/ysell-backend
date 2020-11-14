package com.ysell.modules.orders.dependencies;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.repositories.OrderRepository;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.modules.common.utilities.MapperUtils;
import com.ysell.modules.orders.domain.abstractions.OrderDao;
import com.ysell.modules.orders.enums.OrderStatus;
import com.ysell.modules.orders.models.dto.output.ProductDto;
import com.ysell.modules.orders.models.request.OrderRequest;
import com.ysell.modules.orders.models.request.OrderUpdateRequest;
import com.ysell.modules.orders.models.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaOrderDao implements OrderDao {

	private final OrderRepository orderRepo;	
	private final ProductRepository productRepo;
	private final OrganisationRepository orgRepo;
	private final ModelMapper mapper;

	@Override
	public Optional<ProductDto> getProduct(long productId) {
		return productRepo.findById(productId)
		.map(productEntity ->  mapper.map(productEntity, ProductDto.class));
	}

	@Override
	public OrderResponse saveOrder(OrderRequest request, int status) {
		OrderEntity entity = mapper.map(request, OrderEntity.class);
		return persistOrMergeOrder(entity, status);
	}

	@Override
	public OrderResponse updateOrder(OrderUpdateRequest request, int status) {
		OrderEntity entity = mapper.map(request, OrderEntity.class);
		return persistOrMergeOrder(entity, status);
	}
	
	private OrderResponse persistOrMergeOrder(final OrderEntity entity, int status) {
		entity.getSales().forEach(sale -> sale.setOrder(entity));
		entity.setStatus(status);
		OrderEntity order = orderRepo.save(entity);
		
		OrderResponse response = mapper.map(order, OrderResponse.class);
		response.setStatus(OrderStatus.fromValue(order.getStatus()));
		
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

	@Override
	public ProductDto updateProductStock(long productId, int quantity) {
		ProductEntity product = productRepo.findById(productId).orElse(null);	
		product.setCurrentStock(product.getCurrentStock() + quantity);
		product = productRepo.save(product);

		return mapper.map(product, ProductDto.class);
	}
	
	@Override
	public boolean hasOrganisation(long organisationId) {
		return orgRepo.existsById(organisationId);
	}

	@Override
	public Optional<OrderResponse> getOrder(UUID id) {
		return orderRepo.findById(id)
				.map(orderEntity -> mapper.map(orderEntity, OrderResponse.class));
	}

	@Override
	public OrderResponse updateOrderStatus(UUID id, OrderStatus status) {
		OrderEntity entity = orderRepo.findById(id).orElse(null);
		entity.setStatus(status.getValue());
		entity = orderRepo.save(entity);
		
		OrderResponse response = mapper.map(entity, OrderResponse.class);
		response.setStatus(status);		
	
		return enrichResponseWithNames(response);
	}

	@Override
	public List<OrderResponse> getOrderByOrganisation(long organisationId) {
		List<OrderEntity> orderEntities = orderRepo.findByOrganisationId(organisationId);
		
		return MapperUtils.toStream(mapper, orderEntities, OrderResponse.class, (orderEntity, orderResponse) -> {
			orderResponse.setStatus(OrderStatus.fromValue(orderEntity.getStatus()));
			return orderResponse;
		}).collect(Collectors.toList());
	}
	
}
