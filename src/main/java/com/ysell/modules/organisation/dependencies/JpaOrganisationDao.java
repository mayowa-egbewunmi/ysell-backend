package com.ysell.modules.organisation.dependencies;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.UserRepository;
import com.ysell.modules.common.utilities.MapperUtils;
import com.ysell.modules.organisation.domain.abstractions.OrganisationDao;
import com.ysell.modules.organisation.models.dto.OrganisationDto;
import com.ysell.modules.organisation.models.request.CreateOrganisationRequest;
import com.ysell.modules.organisation.models.request.UpdateOrganisationRequest;
import com.ysell.modules.organisation.models.response.OrganisationResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaOrganisationDao implements OrganisationDao {

	private final OrganisationRepository orgRepo;
	private final UserRepository userRepo;
	private final ModelMapper mapper;

	@Override
	public List<OrganisationResponse> getAllOrganisations() {
		List<OrganisationEntity> entities = orgRepo.findAll();
		return MapperUtils.toStream(mapper, entities, OrganisationResponse.class).collect(Collectors.toList());
	}

	@Override
	public Optional<OrganisationResponse> getOrganisationById(long id) {
		return orgRepo.findById(id)
				.map(org -> mapper.map(org, OrganisationResponse.class));
	}

	@Override
	public OrganisationResponse createOrganisation(CreateOrganisationRequest request) {
		OrganisationEntity entity = mapper.map(request, OrganisationEntity.class);
		entity = orgRepo.save(entity);
		return mapper.map(entity, OrganisationResponse.class);
	}

	@Override
	public OrganisationResponse updateOrganisation(UpdateOrganisationRequest request) {
		OrganisationEntity entity = mapper.map(request, OrganisationEntity.class);
		entity = orgRepo.save(entity);
		return mapper.map(entity, OrganisationResponse.class);
	}

	@Override
	public boolean hasUser(long userId) {
		return userRepo.existsById(userId);
	}

	@Override
	public List<OrganisationResponse> getOrganisationsByUser(long userId) {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(userId);
		return MapperUtils.toStream(mapper, orgRepo.findByUsers(userEntity), OrganisationResponse.class).collect(Collectors.toList());
	}

	@Override
	public Optional<OrganisationDto> getOrganisationByEmail(String email) {
		return orgRepo.findByEmailIgnoreCase(email).stream()
				.findAny()
				.map(org -> new OrganisationDto(org.getId(), org.getEmail()));
	}
}
