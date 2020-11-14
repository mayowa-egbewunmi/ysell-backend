package com.ysell.modules.organisation.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateOrganisationRequest {

	@NotNull
	private Long id;

	@NotEmpty
    private String email;

	@NotEmpty
    private String name;

	@NotEmpty
    private String address;
    
    private String logo;
}
