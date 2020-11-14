package com.ysell.modules.user.domain.abstractions;

import com.ysell.modules.user.models.request.*;
import com.ysell.modules.user.models.response.PasswordChangeResponse;
import com.ysell.modules.user.models.response.UserResponse;

import java.util.List;

public interface UserService {

	UserResponse getUserById(long id);

	List<UserResponse> getAllUsers();

	UserResponse createUser(CreateUserRequest userDetails);

	UserResponse updateUser(UpdateUserRequest userDetails);

	UserResponse unsubscribe(SubscriptionRequest unsubscribeRequest);

	UserResponse resubscribe(SubscriptionRequest subscribeRequest);

	PasswordChangeResponse resetCodeInitiate(ResetInitiateRequest request);

	PasswordChangeResponse resetCodeVerify(ResetVerifyRequest request);

	PasswordChangeResponse resetPassword(ResetPasswordRequest request);

	PasswordChangeResponse changePassword(ChangePasswordRequest request);

	List<UserResponse> getUsersByOrganisation(UsersByOrganisationRequest request);
}
