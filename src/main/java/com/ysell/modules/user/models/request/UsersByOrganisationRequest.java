package com.ysell.modules.user.models.request;

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
public class UsersByOrganisationRequest {

	@Valid
	private Set<LookupDto> organisations = new HashSet<>();
}
