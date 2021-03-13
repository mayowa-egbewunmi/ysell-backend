package com.ysell.modules.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResetCodeValidateRequest {

	@NotEmpty
	private String email;

	@NotEmpty
	private String resetCode;
}
