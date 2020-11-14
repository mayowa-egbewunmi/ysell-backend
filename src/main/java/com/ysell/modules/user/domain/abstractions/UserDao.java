package com.ysell.modules.user.domain.abstractions;

import java.util.List;
import java.util.Optional;

import com.ysell.modules.user.models.dto.input.CreateResetCodeDto;
import com.ysell.modules.user.models.dto.output.ActiveUserDto;
import com.ysell.modules.user.models.dto.output.ResetCodeDto;
import com.ysell.modules.user.models.dto.output.UserPasswordDto;
import com.ysell.modules.user.models.request.CreateUserRequest;
import com.ysell.modules.user.models.request.UpdateUserRequest;
import com.ysell.modules.user.models.response.UserResponse;

public interface UserDao {
	
	Optional<ActiveUserDto> getUserByUsername (String username);
	
	Optional<UserPasswordDto> getUsernameAndPassword(String username);
	
	Optional<ActiveUserDto> getUserById(long id);

	Optional<UserResponse> getUserDetailsById(long id);

	List<UserResponse> getAllUsers();
	
	UserResponse createUser(CreateUserRequest userDetails, String hash);

	UserResponse updateUser(UpdateUserRequest userDetails);
	
	UserResponse updateUserPassword(String email, String hash);
	
	UserResponse unsubscribeUser(long userId);
		
	UserResponse resubscribeUser(long userId);
	
	void saveResetCode(CreateResetCodeDto resetCodeDto);
	
	ResetCodeDto getResetCodeDto(long userId, String code);
	
	void deactivateResetCode(long id);
		
	boolean hasOrganisation(long organisationId);

	List<UserResponse> getUsersByOrganisation(long organisationId);
}
