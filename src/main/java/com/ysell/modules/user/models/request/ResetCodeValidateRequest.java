package com.ysell.modules.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
public class ResetCodeValidateRequest {

	@NotEmpty
	private String email;

	@NotEmpty
	private String resetCode;
}
