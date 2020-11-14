package com.ysell.modules.organisation.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrganisationResponse {

    private long id;

    private String email;
	
    private String name;
    
    private String address;
    
    private String logo;
}
