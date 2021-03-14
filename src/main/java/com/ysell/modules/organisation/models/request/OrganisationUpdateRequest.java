package com.ysell.modules.organisation.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrganisationUpdateRequest {

    private String name;

    private String email;

    private String address;
    
    private String logo;
}
