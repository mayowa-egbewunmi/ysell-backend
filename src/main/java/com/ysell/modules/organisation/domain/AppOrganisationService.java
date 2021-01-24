package com.ysell.modules.organisation.domain;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.UserRepository;
import com.ysell.modules.common.abstractions.BaseCrudService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.organisation.models.request.OrganisationRequest;
import com.ysell.modules.organisation.models.response.OrganisationResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppOrganisationService
		extends BaseCrudService<OrganisationEntity, OrganisationRequest, OrganisationRequest, OrganisationResponse>
		implements OrganisationService {

	private final OrganisationRepository organisationRepo;

	private final UserRepository userRepo;

	private final ModelMapper mapper = new ModelMapper();


	public AppOrganisationService(OrganisationRepository organisationRepo, UserRepository userRepo) {
		super(organisationRepo, OrganisationEntity.class, OrganisationResponse.class);

		this.organisationRepo = organisationRepo;
		this.userRepo = userRepo;
	}


	@Override
	public List<OrganisationResponse> getOrganisationsByUserIds(Set<UUID> userIds) {
		userIds.forEach(userId -> {
			if(!userRepo.existsById(userId))
				ServiceUtils.throwWrongIdException("User", userId);
		});

		return organisationRepo.findByUsersIdIn(userIds).stream()
				.map(organisationEntity -> mapper.map(organisationEntity, OrganisationResponse.class))
				.collect(Collectors.toList());
	}


	@Override
	protected void beforeCreate(OrganisationRequest request) {
		if(organisationRepo.existsByEmailIgnoreCase(request.getEmail()))
			ServiceUtils.throwWrongEmailException("Organisation", request.getEmail());
		if(organisationRepo.existsByNameIgnoreCase(request.getName()))
			ServiceUtils.throwWrongNameException("Organisation", request.getName());
	}


	@Override
	protected void beforeUpdate(UUID organisationId, OrganisationRequest request) {
		organisationRepo.findByEmailIgnoreCase(request.getEmail()).ifPresent(organisationEntity -> {
			if (organisationEntity.getId() != organisationId)
				ServiceUtils.throwWrongEmailException("Organisation", request.getEmail());
		});
		organisationRepo.findByNameIgnoreCase(request.getName()).ifPresent(organisationEntity -> {
			if (organisationEntity.getId() != organisationId)
				ServiceUtils.throwWrongNameException("Organisation", request.getName());
		});
	}
}
