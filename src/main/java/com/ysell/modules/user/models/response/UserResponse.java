package com.ysell.modules.user.models.response;

import com.ysell.modules.common.models.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    
	private long id;
    
	private String name;
    
	private String email;
    
    private Set<LookupDto> organisations;
}
