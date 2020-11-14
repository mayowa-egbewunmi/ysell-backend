package com.ysell.modules.synchronisation.models.request;

import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.synchronisation.models.dto.input.OrderInputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SynchronisationRequest {

	@Valid
	private Set<LookupDto> userOrganisations;

	private Date lastSyncTime;

	@Valid
	private Set<OrderInputDto> orders;
}
