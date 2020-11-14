package com.ysell.modules.user.models.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ActiveUserDto {
	
	private final String username;
	
	private final boolean isActive;
}
