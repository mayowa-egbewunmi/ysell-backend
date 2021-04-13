package com.ysell.modules.synchronisation.domain;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.PaymentEntity;
import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.entities.SaleEntity;
import com.ysell.jpa.entities.base.AuditableEntity;
import com.ysell.jpa.entities.base.ClientAuditableEntity;
import com.ysell.jpa.repositories.*;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.services.LoggedInUserService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.synchronisation.models.dto.*;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest.OrderRequestData;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest.PaymentRequestData;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest.ProductRequestData;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest.SalesRequestData;
import com.ysell.modules.synchronisation.models.response.SynchronisationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
		LocalDateTime startTime = LocalDateTime.now();

		Set<UUID> userOrganisationIds = getLoggedInUserOrganisationIds();

		validateRequest(request, userOrganisationIds);

		ProductResponseData productResponseData = synchroniseProducts(request.getProductData(), userOrganisationIds);
		OrderResponseData orderResponseData = synchroniseOrders(request.getOrderData(), userOrganisationIds);
		SalesResponseData salesResponseData = synchroniseSales(request.getSalesData(), userOrganisationIds);
		PaymentResponseData paymentResponseData = synchronisePayments(request.getPaymentData(), userOrganisationIds);

		LocalDateTime endTime = LocalDateTime.now();

		System.out.println("Start Time: " + startTime);
		System.out.println("End Time: " + endTime);
		System.out.println("Time Difference In Milli-Seconds: " + (startTime.until(endTime, ChronoUnit.MILLIS)));

		return SynchronisationResponse.builder()
				.productData(productResponseData)
				.orderData(orderResponseData)
				.salesData(salesResponseData)
				.paymentData(paymentResponseData)
				.build();
	}


	private Set<UUID> getLoggedInUserOrganisationIds() {
		return loggedInUserService.getLoggedInUser().getOrganisations().stream()
				.map(AuditableEntity::getId)
				.collect(Collectors.toSet());
	}


	private ProductResponseData synchroniseProducts(ProductRequestData productRequestData, Set<UUID> userOrganisationIds) {
		Set<ProductSyncResponseDto> newProducts = getNewProducts(productRequestData.getMostRecentlySyncedTimestamp(), userOrganisationIds);

		Set<ProductSyncResponseDto> updatedProducts = getUpdatedProducts(productRequestData.getMostRecentlySyncedTimestamp(), userOrganisationIds);
		updatedProducts = updatedProducts.stream()
				.filter(x -> productRequestData.getUnsyncedProducts().stream().noneMatch(y -> y.getId().equals(x.getId())))
				.collect(Collectors.toSet());

		Set<UpdatedSyncResponseDto> syncedProducts = syncProducts(productRequestData.getUnsyncedProducts(), userOrganisationIds);

		return ProductResponseData.builder()
				.newProducts(newProducts)
				.updatedProducts(updatedProducts)
				.syncedProducts(syncedProducts)
				.build();
	}


	private OrderResponseData synchroniseOrders(OrderRequestData orderRequestData, Set<UUID> userOrganisationIds) {
		Set<OrderSyncResponseDto> newOrders = getNewOrders(orderRequestData.getMostRecentlySyncedTimestamp(), userOrganisationIds);

		Set<OrderSyncResponseDto> updatedOrders = getUpdatedOrders(orderRequestData.getMostRecentlySyncedTimestamp(), userOrganisationIds);
		updatedOrders = updatedOrders.stream()
				.filter(x -> orderRequestData.getUnsyncedOrders().stream().noneMatch(y -> y.getId().equals(x.getId())))
				.collect(Collectors.toSet());

		Set<UpdatedSyncResponseDto> syncedOrders = syncOrders(orderRequestData.getUnsyncedOrders(), userOrganisationIds);

		return OrderResponseData.builder()
				.newOrders(newOrders)
				.updatedOrders(updatedOrders)
				.syncedOrders(syncedOrders).build();
	}


	private SalesResponseData synchroniseSales(SalesRequestData salesRequestData, Set<UUID> userOrganisationIds) {
		Set<SaleSyncResponseDto> newSales = getNewSales(salesRequestData.getMostRecentlySyncedTimestamp(), userOrganisationIds);

		Set<SaleSyncResponseDto> updatedSales = getUpdatedSales(salesRequestData.getMostRecentlySyncedTimestamp(), userOrganisationIds);
		updatedSales = updatedSales.stream()
				.filter(x -> salesRequestData.getUnsyncedSales().stream().noneMatch(y -> y.getId().equals(x.getId())))
				.collect(Collectors.toSet());

		Set<UpdatedSyncResponseDto> syncedSales = syncSales(salesRequestData.getUnsyncedSales(), userOrganisationIds);

		return SalesResponseData.builder()
				.newSales(newSales)
				.updatedSales(updatedSales)
				.syncedSales(syncedSales)
				.build();
	}


	private PaymentResponseData synchronisePayments(PaymentRequestData paymentRequestData, Set<UUID> userOrganisationIds) {
		Set<PaymentSyncResponseDto> newPayments = getNewPayments(paymentRequestData.getMostRecentlySyncedTimestamp(), userOrganisationIds);

		Set<PaymentSyncResponseDto> updatedPayments = getUpdatedPayments(paymentRequestData.getMostRecentlySyncedTimestamp(), userOrganisationIds);
		updatedPayments = updatedPayments.stream()
				.filter(x -> paymentRequestData.getUnsyncedPayments().stream().noneMatch(y -> y.getId().equals(x.getId())))
				.collect(Collectors.toSet());

		Set<UpdatedSyncResponseDto> syncedPayments = syncedPayments(paymentRequestData.getUnsyncedPayments(), userOrganisationIds);

		return PaymentResponseData.builder()
				.newPayments(newPayments)
				.updatedPayments(updatedPayments)
				.syncedPayments(syncedPayments)
				.build();
	}


	private void validateRequest(SynchronisationRequest request, Set<UUID> userOrganisationIds) {
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
				userOrganisationIds
		);

		validateOrganisationIdsForUser(
				request.getOrderData().getUnsyncedOrders().stream()
						.map(BaseSyncRequestDto::getOrganisationId)
						.collect(Collectors.toSet()),
				userOrganisationIds
		);

		validateOrganisationIdsForUser(
				request.getSalesData().getUnsyncedSales().stream()
						.map(BaseSyncRequestDto::getOrganisationId)
						.collect(Collectors.toSet()),
				userOrganisationIds
		);

		validateOrganisationIdsForUser(
				request.getPaymentData().getUnsyncedPayments().stream()
						.map(BaseSyncRequestDto::getOrganisationId)
						.collect(Collectors.toSet()),
				userOrganisationIds
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


	private Set<UpdatedSyncResponseDto> syncProducts(Set<ProductSyncRequestDto> unsyncedProducts, Set<UUID> userOrganisationIds) {
		return unsyncedProducts.stream().map(unsyncedDto -> {
			ProductEntity entity = productRepository.findById(unsyncedDto.getId())
					.orElse(ProductEntity.builder()
							.currentStock(0)
							.organisation(organisationRepository.getOne(unsyncedDto.getOrganisationId()))
							.build());

			setValuesForNewOrValidateOrganisationForUpdate(entity, unsyncedDto, entity.getOrganisation().getId(), userOrganisationIds);

			entity.setName(unsyncedDto.getName());
			entity.setDescription(unsyncedDto.getName());
			entity.setCostPrice(unsyncedDto.getCostPrice());
			entity.setSellingPrice(unsyncedDto.getSellingPrice());

			setValuesForUpdate(entity, unsyncedDto);

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


	private Set<UpdatedSyncResponseDto> syncOrders(Set<OrderSyncRequestDto> unsyncedOrders, Set<UUID> userOrganisationIds) {
		return unsyncedOrders.stream().map(unsyncedDto -> {
			OrderEntity entity = orderRepository.findById(unsyncedDto.getId())
					.orElse(OrderEntity.builder()
							.organisation(organisationRepository.getOne(unsyncedDto.getOrganisationId()))
							.build());

			setValuesForNewOrValidateOrganisationForUpdate(entity, unsyncedDto, entity.getOrganisation().getId(), userOrganisationIds);

			entity.setTitle(unsyncedDto.getTitle());
			entity.setStatus(unsyncedDto.getStatus());

			setValuesForUpdate(entity, unsyncedDto);

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


	private Set<UpdatedSyncResponseDto> syncSales(Set<SaleSyncRequestDto> unsyncedSales, Set<UUID> userOrganisationIds) {
		return unsyncedSales.stream().map(unsyncedDto -> {
			SaleEntity entity = saleRepository.findById(unsyncedDto.getId())
					.orElse(SaleEntity.builder()
							.order(orderRepository.getOne(unsyncedDto.getOrderId()))
							.product(productRepository.getOne(unsyncedDto.getProductId()))
							.build());

			setValuesForNewOrValidateOrganisationForUpdate(entity, unsyncedDto, entity.getOrder().getOrganisation().getId(), userOrganisationIds);

			entity.setQuantity(unsyncedDto.getQuantity());
			entity.setTotalSellingPrice(unsyncedDto.getTotalSellingPrice());
			entity.setTotalCostPrice(unsyncedDto.getTotalCostPrice());
			entity.setSaleType(unsyncedDto.getSaleType());

			setValuesForUpdate(entity, unsyncedDto);

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


	private Set<UpdatedSyncResponseDto> syncedPayments(Set<PaymentSyncRequestDto> unsyncedPayments, Set<UUID> userOrganisationIds) {
		return unsyncedPayments.stream().map(unsyncedDto -> {
			PaymentEntity entity = paymentRepository.findById(unsyncedDto.getId())
					.orElse(PaymentEntity.builder()
							.order(orderRepository.getOne(unsyncedDto.getOrderId()))
							.build());

			setValuesForNewOrValidateOrganisationForUpdate(entity, unsyncedDto, entity.getOrder().getOrganisation().getId(), userOrganisationIds);

			entity.setMode(unsyncedDto.getMode());
			entity.setAmount(unsyncedDto.getAmount());
			entity.setNarration(unsyncedDto.getNarration());

			setValuesForUpdate(entity, unsyncedDto);

			entity = paymentRepository.save(entity);

			return UpdatedSyncResponseDto.from(entity);
		}).collect(Collectors.toSet());
	}


	private <T extends ClientAuditableEntity> void setValuesForNewOrValidateOrganisationForUpdate(T entity, BaseSyncRequestDto unsyncedDto, UUID organisationId, Set<UUID> userOrganisationIds) {
		if (entity.getId() == null) {
			entity.setId(unsyncedDto.getId());
			entity.setCreatedBy(unsyncedDto.getCreatedBy());
			entity.setCreatedAt(Instant.now());
			entity.setClientCreatedAt(unsyncedDto.getClientCreatedAt());
		} else if (userOrganisationIds.stream().noneMatch(oId -> oId.equals(organisationId))) {
			throw new YSellRuntimeException(String.format(
					"%s with id %s does not belong to your organisation", entity.getTableName(), unsyncedDto.getId()
			));
		}
	}


	private <T extends ClientAuditableEntity> void setValuesForUpdate(T entity, BaseSyncRequestDto unsyncedDto) {
		entity.setUpdatedBy(unsyncedDto.getUpdatedBy());
		entity.setUpdatedAt(Instant.now());
		entity.setClientUpdatedAt(unsyncedDto.getClientUpdatedAt());
	}
}