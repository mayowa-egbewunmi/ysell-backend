package com.ysell.modules.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class SubscriptionRequest {

	@NotNull
	private UUID userId;
}
