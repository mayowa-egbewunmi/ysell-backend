package com.ysell.modules.organisation.domain;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.organisation.domain.abstractions.OrganisationDao;
import com.ysell.modules.organisation.domain.abstractions.OrganisationService;
import com.ysell.modules.organisation.models.dto.OrganisationDto;
import com.ysell.modules.organisation.models.request.CreateOrganisationRequest;
import com.ysell.modules.organisation.models.request.OrganisationsByUserRequest;
import com.ysell.modules.organisation.models.request.UpdateOrganisationRequest;
import com.ysell.modules.organisation.models.response.OrganisationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppOrganisationService implements OrganisationService {

	private final OrganisationDao organisationDao;

	@Override
	public List<OrganisationResponse> getAllOrganisations() {
		return organisationDao.getAllOrganisations();
	}

	@Override
	public OrganisationResponse getOrganisationById(long id) {
		return organisationDao.getOrganisationById(id)
				.orElseThrow(() -> ServiceUtils.wrongIdException("Organisation", id));
	}

	@Override
	public List<OrganisationResponse> getOrganisationsByUser(@Valid OrganisationsByUserRequest request) {
		for (LookupDto user : request.getUsers()) {
			if (!organisationDao.hasUser(user.getId())) {
				throw ServiceUtils.wrongIdException("Organisation", user.getId());
			}
		}

		return request.getUsers().stream()
				.flatMap(user -> organisationDao.getOrganisationsByUser(user.getId()).stream())
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public OrganisationResponse createOrganisation(@Valid CreateOrganisationRequest request) {
		OrganisationDto existingOrg = organisationDao.getOrganisationByEmail(request.getEmail())
				.orElseThrow(() -> new YSellRuntimeException(String.format("Organisation with email %s already exists", request.getEmail())));

		return organisationDao.createOrganisation(request);
	}

	@Override
	@Transactional
	public OrganisationResponse updateOrganisation(@Valid UpdateOrganisationRequest request) {
		OrganisationResponse currentOrg = organisationDao.getOrganisationById(request.getId())
				.orElseThrow(() -> new YSellRuntimeException(String.format("Organisation with Id %d does not exist", request.getId())));

		Optional<OrganisationDto> optSameOrOtherOrg = organisationDao.getOrganisationByEmail(request.getEmail());
		if (optSameOrOtherOrg.isPresent()
				&& optSameOrOtherOrg.get().getId() != currentOrg.getId()
				&& !optSameOrOtherOrg.get().getEmail().equals(currentOrg.getEmail())) {
			throw new YSellRuntimeException(String.format("Another organisation with email %s already exists", request.getEmail()));
		}

		return organisationDao.updateOrganisation(request);
	}
}
