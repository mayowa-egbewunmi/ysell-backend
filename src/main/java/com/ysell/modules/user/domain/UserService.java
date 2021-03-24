package com.ysell.modules.user.domain;

import com.ysell.common.models.YsellResponse;
import com.ysell.modules.common.models.PageWrapper;
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

	PageWrapper<UserResponse> getUsersByPage(Pageable page);

	UserResponse getLoggedInUser();

	UserResponse getUserByEmail(String userEmail);

	UserResponse getById(UUID id);

	List<UserResponse> getUsersByOrganisation(Set<UUID> organisationIds);

	UserRegistrationResponse registerUser(CreateUserRequest userDetails);

	UserRegistrationResponse registerUserWithOrganisation(RegisterUserWithOrganisationRequest request);

	YsellResponse<String> resendVerificationCode(ResendResetCodeRequest request);

	UserResponse completeRegistration(ValidateEmailRequest request);

	UserResponse updateUser(UUID userId, UpdateUserRequest userDetails);

	UserResponse deactivate(ActivationRequest unsubscribeRequest);

	UserResponse reactivate(ActivationRequest subscribeRequest);

	UserResponse softDelete(UserSoftDeleteRequest request);

	UserResponse undelete(UserSoftDeleteRequest request);

	YsellResponse<String> initiatePasswordReset(InitiateResetPasswordRequest request);

	YsellResponse<String> verifyResetCode(VerifyResetCodeRequest request);

	YsellResponse<String> resetPassword(ResetPasswordRequest request);

	YsellResponse<String> changePassword(ChangePasswordRequest request);
}
