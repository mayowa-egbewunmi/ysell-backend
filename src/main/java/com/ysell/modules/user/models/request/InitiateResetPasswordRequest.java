package com.ysell.modules.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
public class InitiateResetPasswordRequest {

	@NotEmpty
	private String email;
}
