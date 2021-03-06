package com.ysell.modules.user.models.request;

import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateUserRequest {

	@NotEmpty
	private String name;

	@NotEmpty
	private String email;

	@NotEmpty
	private String phoneNumber;

	@NotEmpty
	private String password;

	private String bankName;

	private String accountNumber;

	private String accountName;

	@Valid
    private Set<LookupDto> organisations;

	@NotEmpty
	private String clientId;
}
