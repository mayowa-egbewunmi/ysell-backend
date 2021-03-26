package com.ysell.modules.user.models.response;

import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.entities.inactive.InactiveUserEntity;
import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class UserResponse {

	private UUID id;

	private String name;

	private String email;

	private String bankName;

	private String accountNumber;

	private String accountName;

	private boolean activated;

	private Set<LookupDto> organisations;


	public static UserResponse from(UserEntity userEntity) {
		return new UserResponse(
				userEntity.getId(),
				userEntity.getName(),
				userEntity.getEmail(),
				userEntity.getBankName(),
				userEntity.getAccountNumber(),
				userEntity.getAccountName(),
				userEntity.getActivated(),
				userEntity.getOrganisations().stream()
						.map(LookupDto::create)
						.collect(Collectors.toSet())
		);
	}

	public static UserResponse from(InactiveUserEntity inactiveUserEntity) {
		return new UserResponse(
				inactiveUserEntity.getId(),
				inactiveUserEntity.getName(),
				inactiveUserEntity.getEmail(),
				inactiveUserEntity.getBankName(),
				inactiveUserEntity.getAccountNumber(),
				inactiveUserEntity.getAccountName(),
				inactiveUserEntity.getActivated(),
				inactiveUserEntity.getOrganisations().stream()
						.map(LookupDto::create)
						.collect(Collectors.toSet())
		);
	}
}