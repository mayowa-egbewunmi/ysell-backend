package com.ysell.modules.organisation.domain.abstractions;

import java.util.List;
import java.util.Optional;

import com.ysell.modules.organisation.models.dto.OrganisationDto;
import com.ysell.modules.organisation.models.request.CreateOrganisationRequest;
import com.ysell.modules.organisation.models.request.UpdateOrganisationRequest;
import com.ysell.modules.organisation.models.response.OrganisationResponse;

public interface OrganisationDao {

	List<OrganisationResponse> getAllOrganisations();
	
	Optional<OrganisationResponse> getOrganisationById(long id);
	
	boolean hasUser(long userId);

	List<OrganisationResponse> getOrganisationsByUser(long userId);
	
	OrganisationResponse createOrganisation(CreateOrganisationRequest request);
	
	OrganisationResponse updateOrganisation(UpdateOrganisationRequest request);
	
	Optional<OrganisationDto> getOrganisationByEmail(String email);
}
