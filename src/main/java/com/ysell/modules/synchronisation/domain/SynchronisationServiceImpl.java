package com.ysell.modules.synchronisation.domain;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.entities.StockEntity;
import com.ysell.jpa.repositories.OrderRepository;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.jpa.repositories.StockRepository;
import com.ysell.modules.common.dto.LookupDto;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.services.ProductStockService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.synchronisation.models.dto.*;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest;
import com.ysell.modules.synchronisation.models.response.SynchronisationResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

	private final ModelMapper mapper = new ModelMapper();


	@Override
	public SynchronisationResponse synchroniseRecords(SynchronisationRequest request) {
		Set<UUID> organisationIds = request.getUserOrganisations().stream()
				.map(LookupDto::getId)
				.collect(Collectors.toSet());

		validateOrganisations(organisationIds);

		LocalDate lastSyncDate = request.getLastSyncTime();

		SynchronisationResponse response = generateLatestRecords(lastSyncDate, organisationIds);

		saveNewRecords(request, organisationIds);

		return response;
	}


	private void validateOrganisations(Set<UUID> organisationIds) {
		organisationIds.forEach(organisationId -> {
			if (!orgRepo.existsById(organisationId))
				ServiceUtils.throwWrongIdException("Organisation", organisationId);
		});
	}


	private SynchronisationResponse generateLatestRecords(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		SynchronisationResponse response = new SynchronisationResponse();

		response.setNewProducts(getNewProducts(lastSyncDate, organisationIds));
		response.setUpdatedProducts(getUpdatedProducts(lastSyncDate, organisationIds));

		response.setNewOrders(getNewOrders(lastSyncDate, organisationIds));
		response.setUpdatedOrders(getUpdateOrders(lastSyncDate, organisationIds));

		response.setNewStocks(getNewStocks(lastSyncDate, organisationIds));

		response.setLastSyncTime(LocalDate.now());

		return response;
	}


	private Set<OrderResponseDto> getNewOrders(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		return orderRepo.findByCreatedAtGreaterThanAndOrganisationIdIn(lastSyncDate, organisationIds).stream()
				.map(order -> mapper.map(order, OrderResponseDto.class))
				.collect(Collectors.toSet());
	}


	private Set<OrderResponseDto> getUpdateOrders(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		return orderRepo.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds).stream()
				.map(order -> mapper.map(order, OrderResponseDto.class))
				.collect(Collectors.toSet());
	}


	private Set<ProductResponseDto> getNewProducts(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		return productRepo.findByCreatedAtGreaterThanAndOrganisationIdIn(lastSyncDate, organisationIds).stream()
				.map(product -> mapper.map(product, ProductResponseDto.class))
				.collect(Collectors.toSet());
	}


	private Set<ProductResponseDto> getUpdatedProducts(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		return productRepo.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds).stream()
				.map(product -> mapper.map(product, ProductResponseDto.class))
				.collect(Collectors.toSet());
	}


	private Set<StockResponseDto> getNewStocks(LocalDate lastSyncDate, Set<UUID> organisationIds) {
		return stockRepo.findByCreatedAtGreaterThanAndProductOrganisationIdIn(lastSyncDate, organisationIds).stream()
				.map(stock -> mapper.map(stock, StockResponseDto.class))
				.collect(Collectors.toSet());
	}


	private void saveNewRecords(SynchronisationRequest request, Set<UUID> organisationIds) {
		createNewProducts(request.getNewClientProducts(), organisationIds);
		createNewStocks(request.getNewClientStocks(), request.getNewClientProducts(), organisationIds);
		createNewOrders(request.getNewClientOrders(), request.getNewClientProducts(), organisationIds);
	}


	private void createNewProducts(Set<ProductDto> newClientProducts, Set<UUID> organisationIds) {
		newClientProducts.forEach(product -> {
			if (organisationIds.stream().noneMatch(orgId -> orgId == product.getOrganisation().getId()))
				throw new YSellRuntimeException(String.format("Order with id %s does not belong to given user's organisation(s)", product.getId()));

			ProductEntity productEntity = mapper.map(product, ProductEntity.class);
			productRepo.save(productEntity);
		});
	}


	private void createNewStocks(Set<StockDto> newClientStocks, Set<ProductDto> newClientProducts, Set<UUID> organisationIds) {
		List<StockDto> clientStocks = newClientStocks.stream()
				.sorted((a, b) -> b.getQuantity() - a.getQuantity())
				.collect(Collectors.toList());

		clientStocks.forEach(stock -> {
			if (!productRepo.existsByIdAndOrganisationIdIn(stock.getProduct().getId(), organisationIds))
				throw new YSellRuntimeException(String.format(
						"Stock with id %s has a product with id %s that does not belong to given user's organisation(s)", stock.getId(), stock.getProduct().getId()
				));

			boolean isNotNew = isNotNewProductId(stock.getProduct().getId(), newClientProducts);
			if (isNotNew)
				productStockService.updateProductStock(stock.getProduct().getId(), stock.getQuantity());

			ProductEntity productEntity = productRepo.getOne(stock.getProduct().getId());

			int amountToRemove = stock.getQuantity() * -1;
			if (amountToRemove > productEntity.getCurrentStock())
				throw new YSellRuntimeException(String.format("Current Stock for %s is %s. Cannot remove %s", productEntity.getName(), productEntity.getCurrentStock(), amountToRemove));

			StockEntity stockEntity = mapper.map(stock, StockEntity.class);
			stockRepo.save(stockEntity);
		});
	}


	private void createNewOrders(Set<OrderDto> newClientOrders, Set<ProductDto> newClientProducts, Set<UUID> organisationIds) {
		newClientOrders.forEach(order -> {
			if (organisationIds.stream().noneMatch(orgId -> orgId == order.getOrganisation().getId()))
				throw new YSellRuntimeException(String.format("Order with id %s does not belong to given user's organisation(s)", order.getId()));

			order.getSales().forEach(sale -> {
				productStockService.validateProductAndStock(
						order.getOrganisation().getId(),
						sale.getProduct().getId(),
						sale.getQuantity());

				boolean isNotNew = isNotNewProductId(sale.getProduct().getId(), newClientProducts);
				if (isNotNew) {
					int quantityToRemove = sale.getQuantity() * -1;
					productStockService.updateProductStock(sale.getProduct().getId(), quantityToRemove);
				}
			});

			OrderEntity orderEntity = mapper.map(order, OrderEntity.class);
			orderEntity.getSales().forEach(sale -> sale.setOrder(orderEntity));
			orderRepo.save(orderEntity);
		});
	}


	private boolean isNotNewProductId(UUID productId, Set<ProductDto> newClientProducts) {
		return newClientProducts.stream()
				.noneMatch(newClientProduct -> newClientProduct.getId() == productId);
	}
}
