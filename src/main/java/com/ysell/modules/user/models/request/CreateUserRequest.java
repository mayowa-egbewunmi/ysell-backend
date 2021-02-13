package com.ysell.modules.user.models.request;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

	private String bankName;

	private String accountNumber;

	private String accountName;

	@NotNull
	@Valid
    private Set<LookupDto> organisations;
}
