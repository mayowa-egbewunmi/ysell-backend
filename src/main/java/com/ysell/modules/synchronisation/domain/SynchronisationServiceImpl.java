package com.ysell.modules.synchronisation.domain;

import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.repositories.OrderRepository;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.jpa.repositories.StockRepository;
import com.ysell.modules.common.services.LoggedInUserService;
import com.ysell.modules.common.services.ProductStockService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.synchronisation.models.dto.OrderSyncResponseDto;
import com.ysell.modules.synchronisation.models.dto.ProductSyncRequestDto;
import com.ysell.modules.synchronisation.models.dto.ProductSyncResponseDto;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest;
import com.ysell.modules.synchronisation.models.response.SynchronisationResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SynchronisationServiceImpl implements SynchronisationService {

	private final OrganisationRepository orgRepo;

	private final OrderRepository orderRepo;

	private final ProductRepository productRepo;

	private final StockRepository stockRepo;

	private final ProductStockService productStockService;

	private final LoggedInUserService loggedInUserService;

	private final ModelMapper mapper = new ModelMapper();


	@Override
	public SynchronisationResponse synchroniseRecords(SynchronisationRequest request) {
		UserEntity userEntity = loggedInUserService.getLoggedInUser();

		//get new records
		//update records and get ids and created by/at
		//save product, order, sales then payment

		return null;
	}


	private void validateOrganisations(Set<UUID> organisationIds) {
		organisationIds.forEach(organisationId -> {
			if (!orgRepo.existsById(organisationId))
				ServiceUtils.throwWrongIdException("Organisation", organisationId);
		});
	}


	private Set<OrderSyncResponseDto> getNewOrders(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		return orderRepo.findByCreatedAtGreaterThanAndOrganisationIdIn(lastSyncDate, organisationIds).stream()
				.map(order -> mapper.map(order, OrderSyncResponseDto.class))
				.collect(Collectors.toSet());
	}


	private Set<OrderSyncResponseDto> getUpdateOrders(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		return orderRepo.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds).stream()
				.map(order -> mapper.map(order, OrderSyncResponseDto.class))
				.collect(Collectors.toSet());
	}


	private Set<ProductSyncResponseDto> getNewProducts(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		return productRepo.findByCreatedAtGreaterThanAndOrganisationIdIn(lastSyncDate, organisationIds).stream()
				.map(product -> mapper.map(product, ProductSyncResponseDto.class))
				.collect(Collectors.toSet());
	}


	private Set<ProductSyncResponseDto> getUpdatedProducts(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		return productRepo.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds).stream()
				.map(product -> mapper.map(product, ProductSyncResponseDto.class))
				.collect(Collectors.toSet());
	}


	private boolean isNotNewProductId(UUID productId, Set<ProductSyncRequestDto> newClientProducts) {
		return newClientProducts.stream()
				.noneMatch(newClientProduct -> newClientProduct.getId() == productId);
	}
}
