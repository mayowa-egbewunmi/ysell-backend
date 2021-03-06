package com.ysell.modules.organisation;

import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.common.models.PageWrapper;
import com.ysell.modules.organisation.domain.OrganisationService;
import com.ysell.modules.organisation.models.request.OrganisationCreateRequest;
import com.ysell.modules.organisation.models.request.OrganisationUpdateRequest;
import com.ysell.modules.organisation.models.response.OrganisationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(OrganisationController.PATH)
@RequiredArgsConstructor
public class OrganisationController {

    public static final String PATH = ControllerConstants.VERSION_URL + "/organisations";

	private final OrganisationService organisationService;


    @GetMapping
    public PageWrapper<OrganisationResponse> getOrganisationsByPage(
            @PageableDefault(size = ControllerConstants.DEFAULT_PAGE_SIZE) Pageable pageable
    ) {
        return organisationService.getByPage(pageable);
    }


    @GetMapping("/{id}")
    public OrganisationResponse getOrganisation(@PathVariable("id") UUID organisationId){
        return organisationService.getById(organisationId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganisationResponse createOrganisation(@RequestBody @Valid OrganisationCreateRequest request){
        return organisationService.create(request);
    }    


    @PutMapping("/{id}")
    public OrganisationResponse updateOrganisation(@PathVariable("id") UUID organisationId,
                                                   @RequestBody @Valid OrganisationUpdateRequest request){
        return organisationService.update(organisationId, request);
    }


    @GetMapping("/by-user")
    public List<OrganisationResponse> getOrganisationsByUser(@RequestParam("userId") Set<UUID> userIds){
        return organisationService.getOrganisationsByUserIds(userIds);
    }
}
