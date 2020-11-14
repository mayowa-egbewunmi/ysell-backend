package com.ysell.modules.user.models.request;

import com.ysell.modules.common.models.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateUserRequest {

	@NotEmpty
	private String name;

	@NotEmpty
	private String email;

	@NotEmpty
	private String password;
    
    private Set<LookupDto> organisations;
}
