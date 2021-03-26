package com.ysell.modules.organisation.domain;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.UserRepository;
import com.ysell.modules.common.abstractions.BaseCrudService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.organisation.models.request.OrganisationCreateRequest;
import com.ysell.modules.organisation.models.request.OrganisationUpdateRequest;
import com.ysell.modules.organisation.models.response.OrganisationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrganisationServiceImpl
		extends BaseCrudService<OrganisationEntity, OrganisationCreateRequest, OrganisationUpdateRequest, OrganisationResponse>
		implements OrganisationService {

	private final OrganisationRepository organisationRepo;

	private final UserRepository userRepo;


	public OrganisationServiceImpl(OrganisationRepository organisationRepo, UserRepository userRepo) {
		super(organisationRepo, OrganisationEntity.class, OrganisationResponse.class);

		this.organisationRepo = organisationRepo;
		this.userRepo = userRepo;
	}


	@Override
	protected void beforeCreate(OrganisationCreateRequest request) {
		if(organisationRepo.existsByEmailIgnoreCase(request.getEmail()))
			ServiceUtils.throwDuplicateEmailException("Organisation", request.getEmail());
		if(organisationRepo.existsByNameIgnoreCase(request.getName()))
			ServiceUtils.throwDuplicateNameException("Organisation", request.getName());
	}


	@Override
	protected void beforeUpdate(UUID organisationId, OrganisationUpdateRequest request) {
		organisationRepo.findFirstByEmailIgnoreCase(request.getEmail()).ifPresent(existingOrganisation -> {
			if (!existingOrganisation.getId().equals(organisationId))
				ServiceUtils.throwDuplicateEmailException("Organisation", request.getEmail());
		});
		organisationRepo.findFirstByNameIgnoreCase(request.getName()).ifPresent(existingOrganisation -> {
			if (!existingOrganisation.getId().equals(organisationId))
				ServiceUtils.throwDuplicateNameException("Organisation", request.getName());
		});
	}


	@Override
	public List<OrganisationResponse> getOrganisationsByUserIds(Set<UUID> userIds) {
		userIds.forEach(userId -> {
			if(!userRepo.existsById(userId))
				ServiceUtils.throwWrongIdException("User", userId);
		});

		return organisationRepo.findByUsersIdIn(userIds).stream()
				.map(OrganisationResponse::from)
				.collect(Collectors.toList());
	}


	@Override
	protected OrganisationEntity populateUpdateEntity(OrganisationUpdateRequest request, OrganisationEntity entity) {
		if(request.getName() != null)
			entity.setName(request.getName());
		if(request.getEmail() != null)
			entity.setEmail(request.getEmail());
		if(request.getAddress() != null)
			entity.setAddress(request.getAddress());
		if(request.getLogo() != null)
			entity.setLogo(request.getLogo());

		return entity;
	}
}