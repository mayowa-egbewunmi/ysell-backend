package com.ysell.modules.synchronisation.domain;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.models.ClientLookupDto;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.synchronisation.domain.abstraction.SynchronisationDao;
import com.ysell.modules.synchronisation.domain.abstraction.SynchronisationService;
import com.ysell.modules.synchronisation.models.dto.input.OrderInputDto;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest;
import com.ysell.modules.synchronisation.models.response.SynchronisationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppSynchronisationService implements SynchronisationService {

	private final SynchronisationDao syncDao;

	@Override
	@Transactional
	public SynchronisationResponse synchroniseRecords(@Valid SynchronisationRequest request) {
		enrichSalesWithOrder(request);

		Set<Long> organisationIds = request.getUserOrganisations().stream()
				.map(LookupDto::getId)
				.collect(Collectors.toSet());
		validateOrganisations(organisationIds);

		Date lastSyncDate = request.getLastSyncTime();

		SynchronisationResponse response = new SynchronisationResponse();

		response.setNewOrders(syncDao.getLatestCreatedOrders(lastSyncDate, organisationIds));
		response.setUpdatedOrders(syncDao.getLatestUpdatedOrders(lastSyncDate, organisationIds));

		updateOrders(request.getOrders(), organisationIds);

		response.setNewProducts(syncDao.getLatestCreatedProducts(lastSyncDate, organisationIds));
		response.setUpdatedProducts(syncDao.getLatestUpdatedProducts(lastSyncDate, organisationIds));

		response.setLastSyncTime(new Date());

		return response;
	}

	private void enrichSalesWithOrder(SynchronisationRequest request) {
		request.getOrders().forEach(
				order -> order.getSales().forEach(
						sale -> sale.setOrder(ClientLookupDto.create(order.getId()))));
	}

	private void validateOrganisations(Set<Long> organisationIds) {
		for (long organisationId : organisationIds) {
			if (!syncDao.hasOrganisation(organisationId)) {
				throw ServiceUtils.wrongIdException("Organisation", organisationId);
			}
		}
	}

	private void updateOrders(Set<OrderInputDto> orders, Set<Long> organisationIds) {
		for (OrderInputDto order : orders) {
			if (organisationIds.stream().noneMatch(orgId -> orgId == order.getOrganisation().getId()))
				throw new YSellRuntimeException(String.format("Order with id %s does not belong to any user organisation", order.getId()));
		}

		orders.forEach(syncDao::createOrder);
	}
}
