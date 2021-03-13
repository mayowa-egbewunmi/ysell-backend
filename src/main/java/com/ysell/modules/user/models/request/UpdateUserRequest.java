package com.ysell.modules.user.models.request;

import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateUserRequest {

	@NotNull
	private UUID id;

	@NotEmpty
	private String name;

	@NotEmpty
	private String email;

	private String bankName;

	private String accountNumber;

	private String accountName;

	@Valid
    private Set<LookupDto> organisations;
}
