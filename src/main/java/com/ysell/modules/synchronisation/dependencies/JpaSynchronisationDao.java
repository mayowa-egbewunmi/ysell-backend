package com.ysell.modules.synchronisation.dependencies;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.repositories.OrderRepository;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.modules.common.utilities.MapperUtils;
import com.ysell.modules.synchronisation.domain.abstraction.SynchronisationDao;
import com.ysell.modules.synchronisation.models.dto.input.OrderInputDto;
import com.ysell.modules.synchronisation.models.dto.output.OrderResponseDto;
import com.ysell.modules.synchronisation.models.dto.output.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaSynchronisationDao implements SynchronisationDao {

	private final OrganisationRepository orgRepo;	
	private final OrderRepository orderRepo;
	private final ProductRepository productRepo;
	private final ModelMapper mapper;

	@Override
	public boolean hasOrganisation(long organisationId) {
		OrganisationEntity entity = orgRepo.findById(organisationId).orElse(null);
		return entity != null;
	}

	@Override
	public Set<OrderResponseDto> getLatestCreatedOrders(Date lastSyncDate, Set<Long> organisationIds) {
		List<OrderEntity> newOrders = orderRepo.findByCreatedAtGreaterThanAndOrganisationIdIn(lastSyncDate, organisationIds);
		return MapperUtils.toStream(mapper, newOrders, OrderResponseDto.class).collect(Collectors.toSet());
	}

	@Override
	public Set<OrderResponseDto> getLatestUpdatedOrders(Date lastSyncDate, Set<Long> organisationIds) {
		List<OrderEntity> updatedOrders = orderRepo.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds);
		return MapperUtils.toStream(mapper, updatedOrders, OrderResponseDto.class).collect(Collectors.toSet());
	}

	@Override
	public UUID createOrder(OrderInputDto order) {
		OrderEntity entity = mapper.map(order, OrderEntity.class);
		entity = orderRepo.save(entity);
		return entity.getId();
	}

	@Override
	public Set<ProductResponseDto> getLatestCreatedProducts(Date lastSyncDate, Set<Long> organisationIds) {
		List<ProductEntity> newProducts = productRepo.findByCreatedAtGreaterThanAndOrganisationIdIn(lastSyncDate, organisationIds);
		return MapperUtils.toStream(mapper, newProducts, ProductResponseDto.class).collect(Collectors.toSet());
	}

	@Override
	public Set<ProductResponseDto> getLatestUpdatedProducts(Date lastSyncDate, Set<Long> organisationIds) {
		List<ProductEntity> updatedProducts = productRepo.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds);
		return MapperUtils.toStream(mapper, updatedProducts, ProductResponseDto.class).collect(Collectors.toSet());
	}
}
