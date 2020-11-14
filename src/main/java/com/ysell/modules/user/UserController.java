package com.ysell.modules.user;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.common.annotations.WrapResponse;
import com.ysell.config.jwt.models.AppUserDetails;
import com.ysell.config.jwt.service.JwtTokenUtil;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.user.domain.abstractions.UserService;
import com.ysell.modules.user.models.request.*;
import com.ysell.modules.user.models.response.PasswordChangeResponse;
import com.ysell.modules.user.models.response.UserResponse;
import com.ysell.modules.user.models.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ControllerConstants.VERSION_URL + "/users")
@RequiredArgsConstructor
@WrapResponse
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/authenticate")
    @ResponseBody
    public UserTokenResponse authenticate(@RequestBody @Valid LoginRequest request) throws Exception {
        AppUserDetails userDetails;
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthToken);
            userDetails = (AppUserDetails) auth.getPrincipal();
        } catch (BadCredentialsException e) {
            throw new YSellRuntimeException("Invalid Username/Password");
        } catch (DisabledException e) {
            throw new YSellRuntimeException(String.format("User %s is Disabled", request.getEmail()));
        }

        final String token = jwtTokenUtil.generateToken(userDetails.getUsername(), userDetails.getUserId());

        return new UserTokenResponse(token);
    }

    @GetMapping("")
    @ResponseBody
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public UserResponse getUser(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @PostMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("")
    @ResponseBody
    public UserResponse updateUser(@RequestBody UpdateUserRequest request) {
        return userService.updateUser(request);
    }

    @PostMapping("/unsubscribe")
    @ResponseBody
    public UserResponse unsubscribe(@RequestBody SubscriptionRequest request) {
        return userService.unsubscribe(request);
    }

    @PostMapping("/resubscribe")
    @ResponseBody
    public UserResponse resubscribe(@RequestBody SubscriptionRequest request) {
        return userService.resubscribe(request);
    }

    @PostMapping("/code/initiate")
    @ResponseBody
    public PasswordChangeResponse resetCodeInitiate(@RequestBody ResetInitiateRequest request) {
        return userService.resetCodeInitiate(request);
    }

    @PostMapping("/code/verify")
    @ResponseBody
    public PasswordChangeResponse resetCodeVerify(@RequestBody ResetVerifyRequest request) {
        return userService.resetCodeVerify(request);
    }

    @PostMapping("/password/reset")
    @ResponseBody
    public PasswordChangeResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }

    @PostMapping("/password/change")
    @ResponseBody
    public PasswordChangeResponse changePassword(@RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request);
    }

    @GetMapping("/by_organisation")
    @ResponseBody
    public List<UserResponse> getUsersByOrganisation(@RequestParam("id") Set<Long> ids) {
        Set<LookupDto> userLookups = ids.stream()
                .map(id -> LookupDto.create(id))
                .collect(Collectors.toSet());
        UsersByOrganisationRequest request = new UsersByOrganisationRequest(userLookups);

        return userService.getUsersByOrganisation(request);
    }
}
