package com.ysell.modules.organisation;

import com.ysell.common.annotations.WrapResponse;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.organisation.domain.abstractions.OrganisationService;
import com.ysell.modules.organisation.models.request.CreateOrganisationRequest;
import com.ysell.modules.organisation.models.request.OrganisationsByUserRequest;
import com.ysell.modules.organisation.models.request.UpdateOrganisationRequest;
import com.ysell.modules.organisation.models.response.OrganisationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ControllerConstants.VERSION_URL + "/organisations")
@RequiredArgsConstructor
@WrapResponse
public class OrganisationController {

	private final OrganisationService organisationService;

    @GetMapping("")
    @ResponseBody
    public List<OrganisationResponse> getAllOrganisations(){
        return organisationService.getAllOrganisations();
    }
    
    @GetMapping("/by_user")
    @ResponseBody
    public List<OrganisationResponse> getOrganisationsByUser(@RequestParam("id") ArrayList<Long> ids){
    	List<LookupDto> userLookups = ids.stream()
    			.map(id -> LookupDto.create(id))
    			.collect(Collectors.toList());
    	OrganisationsByUserRequest request = new OrganisationsByUserRequest(new HashSet<>(userLookups));
    	
        return organisationService.getOrganisationsByUser(request);
    }
    
    @PostMapping("/{id}")
    @ResponseBody
    public OrganisationResponse getOrganisation(@PathVariable("id") long id){
        return organisationService.getOrganisationById(id);
    }
    
    @PostMapping("")
    @ResponseBody
    public OrganisationResponse createOrganisation(@RequestBody CreateOrganisationRequest request){
        return organisationService.createOrganisation(request);
    }    
    
    @PutMapping("")
    @ResponseBody
    public OrganisationResponse updateOrganisation(@RequestBody UpdateOrganisationRequest request){
        return organisationService.updateOrganisation(request);
    }    
}
