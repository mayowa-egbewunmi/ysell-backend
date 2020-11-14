package com.ysell.modules.organisation.models.request;

import com.ysell.modules.common.models.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrganisationsByUserRequest {

	@Valid
	private Set<LookupDto> users = new HashSet<>();
}
