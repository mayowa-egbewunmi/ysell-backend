package com.ysell.modules.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
public class ChangePasswordRequest {

	@NotEmpty
	private String email;

	@NotEmpty
	private String oldPassword;

	@NotEmpty
	private String newPassword;

	@NotEmpty
	private String newPasswordRepeat;
}
