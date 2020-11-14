package com.ysell.modules.organisation.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateOrganisationRequest {

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String address;
    
    private String logo;
}
