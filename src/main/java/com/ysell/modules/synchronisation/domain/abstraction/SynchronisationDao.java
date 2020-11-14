package com.ysell.modules.synchronisation.domain.abstraction;

import com.ysell.modules.synchronisation.models.dto.input.OrderInputDto;
import com.ysell.modules.synchronisation.models.dto.output.OrderResponseDto;
import com.ysell.modules.synchronisation.models.dto.output.ProductResponseDto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public interface SynchronisationDao {

	boolean hasOrganisation(long organisationId);
	

	Set<OrderResponseDto> getLatestCreatedOrders(Date lastSyncDate, Set<Long> organisationIds);
	
	Set<OrderResponseDto> getLatestUpdatedOrders(Date lastSyncDate, Set<Long> organisationIds);

	UUID createOrder(OrderInputDto order);
	
	Set<ProductResponseDto> getLatestCreatedProducts(Date lastSyncDate, Set<Long> organisationIds);
	
	Set<ProductResponseDto> getLatestUpdatedProducts(Date lastSyncDate, Set<Long> organisationIds);
}
