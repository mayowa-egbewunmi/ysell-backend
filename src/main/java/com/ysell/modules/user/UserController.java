package com.ysell.modules.user;

import com.ysell.common.constants.AppConstants;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.common.dto.PageWrapper;
import com.ysell.modules.common.dto.response.SimpleMessageResponse;
import com.ysell.modules.user.domain.UserService;
import com.ysell.modules.user.models.request.*;
import com.ysell.modules.user.models.response.UserRegistrationResponse;
import com.ysell.modules.user.models.response.UserResponse;
import com.ysell.modules.user.models.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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


    @GetMapping
    public PageWrapper<UserResponse> getAllUsersPaged(@PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) Pageable page) {
        return userService.getAllPaged(page);
    }


    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") UUID userId) {
        return userService.getById(userId);
    }


    @PostMapping(value = "/register")
    public UserRegistrationResponse registerUser(@RequestBody @Valid CreateUserRequest request) {
        return userService.registerUser(request);
    }


    @PostMapping("/complete-registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse completeRegistration(@RequestBody @Valid ValidateEmailRequest request) {
        return userService.completeRegistration(request);
    }


    @PutMapping
    public UserResponse updateUser(@RequestBody @Valid UpdateUserRequest request) {
        return userService.updateUser(request);
    }


    @PostMapping("/unsubscribe")
    public UserResponse unsubscribe(@RequestBody @Valid SubscriptionRequest request) {
        return userService.unsubscribe(request);
    }


    @PostMapping("/resubscribe")
    public UserResponse resubscribe(@RequestBody @Valid SubscriptionRequest request) {
        return userService.resubscribe(request);
    }


    @PostMapping("/code/initiate")
    public SimpleMessageResponse resetCodeInitiate(@RequestBody @Valid InitiateResetPasswordRequest request) {
        return userService.resetCodeInitiate(request);
    }


    @PostMapping("/code/verify")
    public SimpleMessageResponse resetCodeVerify(@RequestBody @Valid ResetCodeValidateRequest request) {
        return userService.resetCodeVerify(request);
    }


    @PostMapping("/password/reset")
    public SimpleMessageResponse resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }


    @PostMapping("/password/change")
    public SimpleMessageResponse changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return userService.changePassword(request);
    }


    @GetMapping("/by-organisation")
    public List<UserResponse> getUsersByOrganisation(@RequestParam("organisationId") Set<UUID> organisationIds) {
        return userService.getUsersByOrganisation(organisationIds);
    }
}
