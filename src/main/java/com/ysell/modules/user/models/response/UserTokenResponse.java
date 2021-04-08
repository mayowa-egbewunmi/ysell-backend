package com.ysell.modules.user.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserTokenResponse {

	private String token;

	@JsonProperty("refresh_token")
	private String refreshToken;
}
