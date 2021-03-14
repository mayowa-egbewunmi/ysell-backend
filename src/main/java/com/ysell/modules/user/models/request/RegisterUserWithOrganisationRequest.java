package com.ysell.modules.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegisterUserWithOrganisationRequest {

	@NotEmpty
	private String name;

	@NotEmpty
	private String email;

	@NotEmpty
	private String phoneNumber;

	@NotEmpty
	private String password;

	@NotEmpty
	private String organisationName;

	private String bankName;

	private String accountNumber;

	private String accountName;
}
