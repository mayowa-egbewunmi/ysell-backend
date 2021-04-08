package com.ysell.modules.user;

import com.ysell.common.constants.AppConstants;
import com.ysell.common.models.YsellResponse;
import com.ysell.config.AuthorityConfig;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.common.models.PageWrapper;
import com.ysell.modules.user.domain.UserService;
import com.ysell.modules.user.models.request.*;
import com.ysell.modules.user.models.response.UserRegistrationResponse;
import com.ysell.modules.user.models.response.UserResponse;
import com.ysell.modules.user.models.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(UserController.PATH)
@RequiredArgsConstructor
public class UserController {

    public static final String PATH = ControllerConstants.VERSION_URL + "/users";

    private final UserService userService;


    @PostMapping("/authenticate")
    public UserTokenResponse authenticate(@RequestBody @Valid LoginRequest request) {
        return userService.authenticate(request);
    }


    @PostMapping("/refresh_token")
    public UserTokenResponse refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return userService.refreshToken(request);
    }


    @GetMapping
    public PageWrapper<UserResponse> getUsersByPage(@PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) Pageable page) {
        return userService.getUsersByPage(page);
    }


    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") UUID userId) {
        return userService.getById(userId);
    }


    @GetMapping("/by-email/{email}")
    public UserResponse getUserByEmail(@PathVariable("email") String userEmail) {
        return userService.getUserByEmail(userEmail);
    }


    @GetMapping("/logged-in")
    public UserResponse getLoggedInUser() {
        return userService.getLoggedInUser();
    }


    @GetMapping("/by-organisation")
    public List<UserResponse> getUsersByOrganisation(@RequestParam("organisationId") Set<UUID> organisationIds) {
        return userService.getUsersByOrganisation(organisationIds);
    }


    @PostMapping(value = "/register")
    public UserRegistrationResponse registerUser(@RequestBody @Valid CreateUserRequest request) {
        return userService.registerUser(request);
    }


    @PostMapping(value = "/register-with-organisation")
    public UserRegistrationResponse registerUserWithOrganisation(@RequestBody @Valid RegisterUserWithOrganisationRequest request) {
        return userService.registerUserWithOrganisation(request);
    }


    @PostMapping("/register/resend-code")
    public YsellResponse<String> resendVerificationCode(@RequestBody @Valid ResendResetCodeRequest request){
        return userService.resendVerificationCode(request);
    }


    @PostMapping("/complete-registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse completeRegistration(@RequestBody @Valid ValidateEmailRequest request) {
        return userService.completeRegistration(request);
    }


    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable("id") UUID userId, @RequestBody @Valid UpdateUserRequest request) {
        return userService.updateUser(userId, request);
    }


    @PostMapping("/deactivate")
    public UserResponse deactivate(@RequestBody @Valid ActivationRequest request) {
        return userService.deactivate(request);
    }


    @PostMapping("/reactivate")
    public UserResponse reactivate(@RequestBody @Valid ActivationRequest request) {
        return userService.reactivate(request);
    }


    @PostMapping("/soft-delete")
    @PreAuthorize("hasAuthority('" + AuthorityConfig.SOFT_DELETE + "')")
    public UserResponse softDelete(@RequestBody @Valid UserSoftDeleteRequest request) {
        return userService.softDelete(request);
    }


    @PostMapping("/undelete")
    @PreAuthorize("hasAuthority('" + AuthorityConfig.UNDELETE + "')")
    public UserResponse undelete(@RequestBody @Valid UserSoftDeleteRequest request) {
        return userService.undelete(request);
    }


    @PostMapping("/code/initiate")
    public YsellResponse<String> initiatePasswordReset(@RequestBody @Valid InitiateResetPasswordRequest request) {
        return userService.initiatePasswordReset(request);
    }


    @PostMapping("/code/verify")
    public YsellResponse<String> verifyResetCode(@RequestBody @Valid VerifyResetCodeRequest request) {
        return userService.verifyResetCode(request);
    }


    @PostMapping("/password/reset")
    public YsellResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }


    @PostMapping("/password/change")
    public YsellResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return userService.changePassword(request);
    }
}
