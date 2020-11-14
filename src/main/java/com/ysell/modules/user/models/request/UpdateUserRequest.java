package com.ysell.modules.user.models.request;

import com.ysell.modules.common.models.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateUserRequest {

	@NotNull
	private Long id;

	@NotEmpty
	private String name;

	@NotEmpty
	private String email;
    
    private Set<LookupDto> organisations;
}
