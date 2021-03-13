package com.ysell.modules.organisation.models.response;

import com.ysell.jpa.entities.OrganisationEntity;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrganisationResponse {

    private UUID id;

    private String email;
	
    private String name;
    
    private String address;
    
    private String logo;


    public static OrganisationResponse from(OrganisationEntity organisationEntity) {
        return OrganisationResponse.builder()
                .id(organisationEntity.getId())
                .email(organisationEntity.getEmail())
                .name(organisationEntity.getName())
                .address(organisationEntity.getAddress())
                .logo(organisationEntity.getLogo())
                .build();
    }
}
