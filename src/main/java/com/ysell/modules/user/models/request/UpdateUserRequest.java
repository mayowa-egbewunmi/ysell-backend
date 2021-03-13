package com.ysell.modules.user.models.request;

import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateUserRequest {

	private String name;

	private String email;

	private String bankName;

	private String accountNumber;

	private String accountName;

	@Valid
    private Set<LookupDto> organisations;
}
