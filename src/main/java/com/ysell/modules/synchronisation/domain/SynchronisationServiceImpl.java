package com.ysell.modules.synchronisation.domain;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.PaymentEntity;
import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.entities.SaleEntity;
import com.ysell.jpa.entities.base.AuditableEntity;
import com.ysell.jpa.repositories.*;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.services.LoggedInUserService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.synchronisation.models.dto.*;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest;
import com.ysell.modules.synchronisation.models.response.SynchronisationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ysell.modules.synchronisation.models.response.SynchronisationResponse.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SynchronisationServiceImpl implements SynchronisationService {

	private final ProductRepository productRepository;

	private final OrderRepository orderRepository;

	private final SaleRepository saleRepository;

	private final PaymentRepository paymentRepository;

	private final OrganisationRepository organisationRepository;

	private final LoggedInUserService loggedInUserService;


	@Override
	public SynchronisationResponse synchroniseRecords(SynchronisationRequest request) {
		Set<UUID> userOrganisationIds = loggedInUserService.getLoggedInUser().getOrganisations().stream()
				.map(AuditableEntity::getId)
				.collect(Collectors.toSet());

		validateRequest(request, userOrganisationIds);

		Set<ProductSyncResponseDto> newProducts = getNewProducts(request.getProductData().getMostRecentlySyncedTimestamp(), userOrganisationIds);
		Set<ProductSyncResponseDto> updatedProducts = getUpdatedProducts(request.getProductData().getMostRecentlySyncedTimestamp(), userOrganisationIds);
		updatedProducts = updatedProducts.stream()
				.filter(x -> request.getProductData().getUnsyncedProducts().stream().noneMatch(y -> y.getId() == x.getId()))
				.collect(Collectors.toSet());
		Set<UpdatedSyncResponseDto> syncedProducts = syncProducts(request.getProductData().getUnsyncedProducts());

		Set<OrderSyncResponseDto> newOrders = getNewOrders(request.getOrderData().getMostRecentlySyncedTimestamp(), userOrganisationIds);
		Set<OrderSyncResponseDto> updatedOrders = getUpdatedOrders(request.getOrderData().getMostRecentlySyncedTimestamp(), userOrganisationIds);
		updatedOrders = updatedOrders.stream()
				.filter(x -> request.getOrderData().getUnsyncedOrders().stream().noneMatch(y -> y.getId() == x.getId()))
				.collect(Collectors.toSet());
		Set<UpdatedSyncResponseDto> syncedOrders = syncOrders(request.getOrderData().getUnsyncedOrders());

		Set<SaleSyncResponseDto> newSales = getNewSales(request.getSalesData().getMostRecentlySyncedTimestamp(), userOrganisationIds);
		Set<SaleSyncResponseDto> updatedSales = getUpdatedSales(request.getSalesData().getMostRecentlySyncedTimestamp(), userOrganisationIds);
		updatedSales = updatedSales.stream()
				.filter(x -> request.getSalesData().getUnsyncedSales().stream().noneMatch(y -> y.getId() == x.getId()))
				.collect(Collectors.toSet());
		Set<UpdatedSyncResponseDto> syncedSales = syncSales(request.getSalesData().getUnsyncedSales());

		Set<PaymentSyncResponseDto> newPayments = getNewPayments(request.getPaymentData().getMostRecentlySyncedTimestamp(), userOrganisationIds);
		Set<PaymentSyncResponseDto> updatedPayments = getUpdatedPayments(request.getPaymentData().getMostRecentlySyncedTimestamp(), userOrganisationIds);
		updatedPayments = updatedPayments.stream()
				.filter(x -> request.getPaymentData().getUnsyncedPayments().stream().noneMatch(y -> y.getId() == x.getId()))
				.collect(Collectors.toSet());
		Set<UpdatedSyncResponseDto> syncedPayments = syncedPayments(request.getPaymentData().getUnsyncedPayments());

		return builder()
				.productData(ProductResponseData.builder()
						.newProducts(newProducts).updatedProducts(updatedProducts).syncedProducts(syncedProducts).build())
				.orderData(OrderResponseData.builder()
						.newOrders(newOrders).updatedOrders(updatedOrders).syncedOrders(syncedOrders).build())
				.salesData(SalesResponseData.builder()
						.newSales(newSales).updatedSales(updatedSales).syncedSales(syncedSales).build())
				.paymentData(PaymentResponseData.builder()
						.newPayments(newPayments).updatedPayments(updatedPayments).syncedPayments(syncedPayments).build())
				.build();
	}


	private void validateRequest(SynchronisationRequest request, Set<UUID> userOrganisations) {
		if (request.getProductData().getUnsyncedProducts().size() != request.getProductData().getCount())
			throwInvalidCountError("Product", request.getProductData().getUnsyncedProducts().size(), request.getProductData().getCount());

		if (request.getOrderData().getUnsyncedOrders().size() != request.getOrderData().getCount())
			throwInvalidCountError("Order", request.getOrderData().getUnsyncedOrders().size(), request.getOrderData().getCount());

		if (request.getSalesData().getUnsyncedSales().size() != request.getSalesData().getCount())
			throwInvalidCountError("Sales", request.getSalesData().getUnsyncedSales().size(), request.getSalesData().getCount());

		if (request.getPaymentData().getUnsyncedPayments().size() != request.getPaymentData().getCount())
			throwInvalidCountError("Payment", request.getPaymentData().getUnsyncedPayments().size(), request.getPaymentData().getCount());

		validateOrganisationIdsForUser(
				request.getProductData().getUnsyncedProducts().stream()
						.map(BaseSyncRequestDto::getOrganisationId)
						.collect(Collectors.toSet()),
				userOrganisations
		);

		validateOrganisationIdsForUser(
				request.getOrderData().getUnsyncedOrders().stream()
						.map(BaseSyncRequestDto::getOrganisationId)
						.collect(Collectors.toSet()),
				userOrganisations
		);

		validateOrganisationIdsForUser(
				request.getSalesData().getUnsyncedSales().stream()
						.map(BaseSyncRequestDto::getOrganisationId)
						.collect(Collectors.toSet()),
				userOrganisations
		);

		validateOrganisationIdsForUser(
				request.getPaymentData().getUnsyncedPayments().stream()
						.map(BaseSyncRequestDto::getOrganisationId)
						.collect(Collectors.toSet()),
				userOrganisations
		);
	}


	private void throwInvalidCountError(String modelName, int size, long count) {
		throw new YSellRuntimeException(String.format(
				"%s size was %s but the count from client was %s", modelName, size, count
		));
	}


	private void validateOrganisationIdsForUser(Set<UUID> organisationIds, Set<UUID> userOrganisationIds) {
		organisationIds.forEach(organisationId -> {
			if (userOrganisationIds.stream().noneMatch(orgId -> orgId.equals(organisationId)))
				ServiceUtils.throwNoAccessIdException("Organisation", organisationId);
		});
	}


	private Set<ProductSyncResponseDto> getNewProducts(Instant lastSyncDate, Set<UUID> organisationIds) {
		return productRepository.findByCreatedAtGreaterThanAndOrganisationIdIn(lastSyncDate, organisationIds).stream()
				.map(ProductSyncResponseDto::from)
				.collect(Collectors.toSet());
	}


	private Set<ProductSyncResponseDto> getUpdatedProducts(Instant lastSyncDate, Set<UUID> organisationIds) {
		return productRepository.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds).stream()
				.map(ProductSyncResponseDto::from)
				.collect(Collectors.toSet());
	}


	private Set<UpdatedSyncResponseDto> syncProducts(Set<ProductSyncRequestDto> unsyncedProducts) {
		return unsyncedProducts.stream().map(unsyncedDto -> {
			ProductEntity entity = ProductEntity.builder()
					.name(unsyncedDto.getName())
					.description(unsyncedDto.getName())
					.costPrice(unsyncedDto.getCostPrice())
					.sellingPrice(unsyncedDto.getSellingPrice())
					.currentStock(0)
					.organisation(organisationRepository.getOne(unsyncedDto.getOrganisationId()))
					.build();

			entity.setId(unsyncedDto.getId());
			entity.setCreatedBy(unsyncedDto.getCreatedBy());
			entity.setUpdatedBy(unsyncedDto.getUpdatedBy());
			entity.setCreatedAt(Instant.now());
			entity.setUpdatedAt(Instant.now());
			entity.setClientCreatedAt(unsyncedDto.getClientCreatedAt());
			entity.setClientUpdatedAt(unsyncedDto.getClientUpdatedAt());

			entity = productRepository.save(entity);

			return UpdatedSyncResponseDto.from(entity);
		}).collect(Collectors.toSet());
	}


	private Set<OrderSyncResponseDto> getNewOrders(Instant lastSyncDate, Set<UUID> organisationIds) {
		return orderRepository.findByCreatedAtGreaterThanAndOrganisationIdIn(lastSyncDate, organisationIds).stream()
				.map(OrderSyncResponseDto::from)
				.collect(Collectors.toSet());
	}


	private Set<OrderSyncResponseDto> getUpdatedOrders(Instant lastSyncDate, Set<UUID> organisationIds) {
		return orderRepository.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds).stream()
				.map(OrderSyncResponseDto::from)
				.collect(Collectors.toSet());
	}


	private Set<UpdatedSyncResponseDto> syncOrders(Set<OrderSyncRequestDto> unsyncedOrders) {
		return unsyncedOrders.stream().map(unsyncedDto -> {
			OrderEntity entity = OrderEntity.builder()
					.title(unsyncedDto.getTitle())
					.status(unsyncedDto.getStatus())
					.organisation(organisationRepository.getOne(unsyncedDto.getOrganisationId()))
					.build();

			entity.setId(unsyncedDto.getId());
			entity.setCreatedBy(unsyncedDto.getCreatedBy());
			entity.setUpdatedBy(unsyncedDto.getUpdatedBy());
			entity.setCreatedAt(Instant.now());
			entity.setUpdatedAt(Instant.now());
			entity.setClientCreatedAt(unsyncedDto.getClientCreatedAt());
			entity.setClientUpdatedAt(unsyncedDto.getClientUpdatedAt());

			entity = orderRepository.save(entity);

			return UpdatedSyncResponseDto.from(entity);
		}).collect(Collectors.toSet());
	}


	private Set<SaleSyncResponseDto> getNewSales(Instant lastSyncDate, Set<UUID> organisationIds) {
		return saleRepository.findByCreatedAtGreaterThanAndOrder_OrganisationIdIn(lastSyncDate, organisationIds).stream()
				.map(SaleSyncResponseDto::from)
				.collect(Collectors.toSet());
	}


	private Set<SaleSyncResponseDto> getUpdatedSales(Instant lastSyncDate, Set<UUID> organisationIds) {
		return saleRepository.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrder_OrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds).stream()
				.map(SaleSyncResponseDto::from)
				.collect(Collectors.toSet());
	}


	private Set<UpdatedSyncResponseDto> syncSales(Set<SaleSyncRequestDto> unsyncedSales) {
		return unsyncedSales.stream().map(unsyncedDto -> {
			SaleEntity entity = SaleEntity.builder()
					.order(orderRepository.getOne(unsyncedDto.getOrderId()))
					.product(productRepository.getOne(unsyncedDto.getProductId()))
					.quantity(unsyncedDto.getQuantity())
					.totalSellingPrice(unsyncedDto.getTotalSellingPrice())
					.totalCostPrice(unsyncedDto.getTotalCostPrice())
					.saleType(unsyncedDto.getSaleType())
					.build();

			entity.setId(unsyncedDto.getId());
			entity.setCreatedBy(unsyncedDto.getCreatedBy());
			entity.setUpdatedBy(unsyncedDto.getUpdatedBy());
			entity.setCreatedAt(Instant.now());
			entity.setUpdatedAt(Instant.now());
			entity.setClientCreatedAt(unsyncedDto.getClientCreatedAt());
			entity.setClientUpdatedAt(unsyncedDto.getClientUpdatedAt());

			entity = saleRepository.save(entity);

			return UpdatedSyncResponseDto.from(entity);
		}).collect(Collectors.toSet());
	}


	private Set<PaymentSyncResponseDto> getNewPayments(Instant lastSyncDate, Set<UUID> organisationIds) {
		return paymentRepository.findByCreatedAtGreaterThanAndOrder_OrganisationIdIn(lastSyncDate, organisationIds).stream()
				.map(PaymentSyncResponseDto::from)
				.collect(Collectors.toSet());
	}


	private Set<PaymentSyncResponseDto> getUpdatedPayments(Instant lastSyncDate, Set<UUID> organisationIds) {
		return paymentRepository.findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrder_OrganisationIdIn(lastSyncDate, lastSyncDate, organisationIds).stream()
				.map(PaymentSyncResponseDto::from)
				.collect(Collectors.toSet());
	}


	private Set<UpdatedSyncResponseDto> syncedPayments(Set<PaymentSyncRequestDto> unsyncedPayments) {
		return unsyncedPayments.stream().map(unsyncedDto -> {
			PaymentEntity entity = PaymentEntity.builder()
					.order(orderRepository.getOne(unsyncedDto.getOrderId()))
					.mode(unsyncedDto.getMode())
					.amount(unsyncedDto.getAmount())
					.narration(unsyncedDto.getNarration())
					.build();

			entity.setId(unsyncedDto.getId());
			entity.setCreatedBy(unsyncedDto.getCreatedBy());
			entity.setUpdatedBy(unsyncedDto.getUpdatedBy());
			entity.setCreatedAt(Instant.now());
			entity.setUpdatedAt(Instant.now());
			entity.setClientCreatedAt(unsyncedDto.getClientCreatedAt());
			entity.setClientUpdatedAt(unsyncedDto.getClientUpdatedAt());

			entity = paymentRepository.save(entity);

			return UpdatedSyncResponseDto.from(entity);
		}).collect(Collectors.toSet());
	}
}