package com.ysell.modules.user.domain;

import com.ysell.modules.common.dto.PageWrapper;
import com.ysell.modules.common.dto.response.SimpleMessageResponse;
import com.ysell.modules.user.models.request.*;
import com.ysell.modules.user.models.response.UserRegistrationResponse;
import com.ysell.modules.user.models.response.UserResponse;
import com.ysell.modules.user.models.response.UserTokenResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {

	UserTokenResponse authenticate(LoginRequest request);

	PageWrapper<UserResponse> getAllPaged(Pageable page);

	UserResponse getById(UUID id);

	List<UserResponse> getUsersByOrganisation(Set<UUID> organisationIds);

	UserRegistrationResponse registerUser(CreateUserRequest userDetails);

	UserResponse updateUser(UpdateUserRequest userDetails);

	UserResponse unsubscribe(SubscriptionRequest unsubscribeRequest);

	UserResponse resubscribe(SubscriptionRequest subscribeRequest);

	SimpleMessageResponse resetCodeInitiate(ResetInitiateRequest request);

	SimpleMessageResponse resetCodeVerify(ResetVerifyRequest request);

	SimpleMessageResponse resetPassword(ResetPasswordRequest request);

	SimpleMessageResponse changePassword(ChangePasswordRequest request);

	UserResponse completeRegistration(ValidateEmailRequest request);
}
