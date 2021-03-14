package com.ysell.modules.organisation.domain;

import com.ysell.modules.common.abstractions.CrudService;
import com.ysell.modules.organisation.models.request.OrganisationCreateRequest;
import com.ysell.modules.organisation.models.request.OrganisationUpdateRequest;
import com.ysell.modules.organisation.models.response.OrganisationResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrganisationService extends CrudService<OrganisationCreateRequest, OrganisationUpdateRequest, OrganisationResponse> {

	List<OrganisationResponse> getOrganisationsByUserIds(Set<UUID> userIds);
}
