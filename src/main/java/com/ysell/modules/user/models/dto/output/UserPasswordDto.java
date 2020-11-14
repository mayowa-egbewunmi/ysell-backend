package com.ysell.modules.user.models.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserPasswordDto {

	private final long id;
	
	private final String username;
	
	private final String hashedPassword;
	
	private final boolean isActive;
}
