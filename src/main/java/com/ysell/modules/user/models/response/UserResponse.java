package com.ysell.modules.user.models.response;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserResponse {
    
	private UUID id;
    
	private String name;
    
	private String email;
    
    private Set<LookupDto> organisations;
}
