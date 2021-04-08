package com.ysell.modules.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshTokenRequest {

	@NotEmpty
	private String token;

	@NotEmpty
	private String refreshToken;

	@NotEmpty
	private String clientSecret;
}
