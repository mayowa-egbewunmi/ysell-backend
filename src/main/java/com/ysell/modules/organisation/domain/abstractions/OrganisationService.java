package com.ysell.modules.organisation.domain.abstractions;

import com.ysell.modules.organisation.models.request.CreateOrganisationRequest;
import com.ysell.modules.organisation.models.request.OrganisationsByUserRequest;
import com.ysell.modules.organisation.models.request.UpdateOrganisationRequest;
import com.ysell.modules.organisation.models.response.OrganisationResponse;

import java.util.List;

public interface OrganisationService {

	List<OrganisationResponse> getAllOrganisations();

	OrganisationResponse getOrganisationById(long id);

	List<OrganisationResponse> getOrganisationsByUser(OrganisationsByUserRequest request);

	OrganisationResponse createOrganisation(CreateOrganisationRequest request);

	OrganisationResponse updateOrganisation(UpdateOrganisationRequest request);
}
