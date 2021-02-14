package com.ysell.modules.user.models.response;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    
	private UUID id;
    
	private String name;
    
	private String email;

	private boolean activated;
    
    private Set<LookupDto> organisations;
}
