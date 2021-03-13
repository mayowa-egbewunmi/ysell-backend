package com.ysell.modules.user.domain;

import com.ysell.common.models.YsellResponse;
import com.ysell.config.jwt.models.AppUserDetails;
import com.ysell.config.jwt.service.JwtTokenUtil;
import com.ysell.jpa.entities.ResetCodeEntity;
import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ResetCodeRepository;
import com.ysell.jpa.repositories.UserRepository;
import com.ysell.modules.common.dtos.LookupDto;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.models.PageWrapper;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.common.utilities.email.EmailSender;
import com.ysell.modules.common.utilities.email.models.EmailModel;
import com.ysell.modules.user.models.request.*;
import com.ysell.modules.user.models.response.UserRegistrationResponse;
import com.ysell.modules.user.models.response.UserResponse;
import com.ysell.modules.user.models.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;

	private final OrganisationRepository orgRepo;

	private final ResetCodeRepository resetCodeRepo;

	private final PasswordEncoder passwordEncoder;

	private final EmailSender emailSender;

	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;

	@Value("${ysell.constants.user-activation.reset-code-delay-in-minutes:30}")
	private int resetCodeDelayInMinutes;


	public UserTokenResponse authenticate(LoginRequest request) {
		try {
			UsernamePasswordAuthenticationToken usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
			Authentication auth = authenticationManager.authenticate(usernamePasswordAuthToken);
			AppUserDetails userDetails = (AppUserDetails) auth.getPrincipal();
			final String token = jwtTokenUtil.generateToken(userDetails.getUsername(), userDetails.getUserId());

			return new UserTokenResponse(token);
		} catch (BadCredentialsException e) {
			throw new YSellRuntimeException("Invalid username/password");
		} catch (DisabledException e) {
			throw new YSellRuntimeException(String.format("User %s is Disabled", request.getEmail()));
		}
	}


	@Override
	public PageWrapper<UserResponse> getUsersByPage(Pageable page) {
		return PageWrapper.from(
				userRepo.findAll(page)
						.map(UserResponse::from)
		);
	}


	@Override
	public UserResponse getById(UUID userId) {
		return userRepo.findById(userId)
				.map(UserResponse::from)
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", userId));
	}


	@Override
	public List<UserResponse> getUsersByOrganisation(Set<UUID> organisationIds) {
		validateOrganisationIds(organisationIds);

		return organisationIds.stream()
				.flatMap(organisationId -> userRepo.findByOrganisationsId(organisationId).stream())
				.map(UserResponse::from)
				.collect(Collectors.toList());
	}


	@Override
	public UserRegistrationResponse registerUser(CreateUserRequest request) {
		validateUserRegistrationRequest(request);

		String hash = passwordEncoder.encode(request.getPassword());

		UserEntity userEntity = performInitialUserCreation(request, hash);

		String resetCode = generateResetCode(userEntity);
		String msg = format("Your verification code is: %s. It expires in %d minutes", resetCode, resetCodeDelayInMinutes);
		EmailModel emailModel = new EmailModel(request.getEmail(), "Ysell Email Validation", msg, null);
		emailSender.send(emailModel, "user verification code");

		final String token = jwtTokenUtil.generateToken(userEntity.getEmail(), userEntity.getId());

		return new UserRegistrationResponse(
				format("Verification code has been sent to your email - %s. It will expire in %s minutes",
						request.getEmail(),
						resetCodeDelayInMinutes),
				token,
				false
		);
	}


	@Override
	public YsellResponse<String> resendVerificationCode(ResendResetCodeRequest request) {
		UserEntity userEntity = getUser(request.getEmail());
		resetCodeRepo.deleteByUserId(userEntity.getId());

		String resetCode = generateResetCode(userEntity);
		String msg = format("Your verification code is: %s. It expires in %d minutes", resetCode, resetCodeDelayInMinutes);
		EmailModel emailModel = new EmailModel(request.getEmail(), "Ysell Email Validation", msg, null);
		emailSender.send(emailModel, "user verification code");

		return YsellResponse.createSuccess(format(
				"Verification code has been sent to your email - %s. It will expire in %s minutes", request.getEmail(), resetCodeDelayInMinutes
		));
	}


	@Override
	public UserResponse completeRegistration(ValidateEmailRequest request) {
		ResetCodeEntity resetCodeEntity = verifyCode(request.getEmail(), request.getCode());
		resetCodeRepo.deleteByUserId(resetCodeEntity.getUser().getId());
		UserEntity userEntity = getUser(request.getEmail());

		return reactivate(new SubscriptionRequest(userEntity.getId()));
	}


	@Override
	public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
		userRepo.findFirstByEmailIgnoreCase(request.getEmail()).ifPresent(existingUser -> {
			if (!existingUser.getId().equals(userId))
				ServiceUtils.throwWrongEmailException("User", request.getEmail());
		});
		userRepo.findFirstByNameIgnoreCase(request.getName()).ifPresent(existingUser -> {
			if (!existingUser.getId().equals(userId))
				ServiceUtils.throwWrongNameException("User", request.getName());
		});

		validateOrganisations(request.getOrganisations());

		return performUserUpdate(userId, request);
	}


	@Override
	public UserResponse deactivate(SubscriptionRequest request) {
		UserEntity user = userRepo.findById(request.getUserId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", request.getUserId()));

		if (!user.getActivated())
			throw new YSellRuntimeException(format("Account with Id %s is already unsubscribed", request.getUserId()));

		user.setActivated(false);
		user = userRepo.save(user);

		return UserResponse.from(user);
	}


	@Override
	public UserResponse reactivate(SubscriptionRequest request) {
		UserEntity user = userRepo.findById(request.getUserId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", request.getUserId()));

		if (user.getActivated())
			throw new YSellRuntimeException(format("Account with Id %s is already subscribed", request.getUserId()));

		user.setActivated(true);
		user = userRepo.save(user);

		return UserResponse.from(user);
	}


	@Override
	public YsellResponse<String> initiatePasswordReset(InitiateResetPasswordRequest request) {
		UserEntity userEntity = getUser(request.getEmail());

		String resetCode = generateResetCode(userEntity);

		String msg = format("Your password reset code is %s. It will expire in %d minutes", resetCode, resetCodeDelayInMinutes);
		EmailModel emailModel = new EmailModel(request.getEmail(), "YSell Password Reset Code", msg, null);
		emailSender.send(emailModel, "reset code");

		return YsellResponse.createSuccess(format("Reset code has been sent to your email. It will expire in %d minutes", resetCodeDelayInMinutes));
	}


	@Override
	public YsellResponse<String> verifyResetCode(VerifyResetCodeRequest request) {
		verifyCode(request.getEmail(), request.getResetCode());
		return YsellResponse.createSuccess("Valid reset code");
	}


	@Override
	public YsellResponse<String> resetPassword(ResetPasswordRequest request) {
		ResetCodeEntity resetCodeEntity = verifyCode(request.getEmail(), request.getResetCode());
		resetCodeRepo.deleteByUserId(resetCodeEntity.getUser().getId());

		updateUserPassword(request.getNewPassword(), request.getEmail());

		return YsellResponse.createSuccess("Password successfully reset");
	}


	@Override
	public YsellResponse<String> changePassword(ChangePasswordRequest request) {
		UserEntity userEntity = getUser(request.getEmail());

		if (!passwordEncoder.matches(request.getOldPassword(), userEntity.getHash()))
			throw new YSellRuntimeException("Old password does not match current password");

		updateUserPassword(request.getNewPassword(), request.getEmail());

		return YsellResponse.createSuccess("Password successfully changed");
	}


	private void validateUserRegistrationRequest(CreateUserRequest request) {
		if (userRepo.existsByEmailIgnoreCase(request.getEmail()))
			ServiceUtils.throwWrongEmailException("User", request.getEmail());
		if (userRepo.existsByNameIgnoreCase(request.getName()))
			ServiceUtils.throwWrongNameException("User", request.getName());

		validateOrganisations(request.getOrganisations());
	}


	private String generateResetCode(UserEntity userEntity) {
		String resetCode = composeResetCode();

		ResetCodeEntity resetCodeEntity = new ResetCodeEntity(userEntity, resetCode, getExpiryTime());
		resetCodeRepo.save(resetCodeEntity);

		return resetCode;
	}


	private UserEntity performInitialUserCreation(CreateUserRequest userDetails, String hash) {
		UserEntity user = UserEntity.builder()
				.name(userDetails.getName())
				.email(userDetails.getEmail())
				.bankName(userDetails.getBankName())
				.accountName(userDetails.getAccountName())
				.accountNumber(userDetails.getAccountNumber())
				.activated(false)
				.organisations(userDetails.getOrganisations().stream()
						.map(o -> orgRepo.getOne(o.getId()))
						.collect(Collectors.toSet()))
				.build();

		user.setHash(hash);

		return userRepo.save(user);
	}


	private UserResponse performUserUpdate(UUID userId, UpdateUserRequest userDetails) {
		UserEntity user = userRepo.findById(userId)
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", userId));

		if (userDetails.getName() != null) {
			user.setName(userDetails.getName());
		}
		if (userDetails.getEmail() != null) {
			user.setEmail(userDetails.getEmail());
		}
		if (userDetails.getBankName() != null) {
			user.setBankName(userDetails.getBankName());
		}
		if (userDetails.getAccountName() != null) {
			user.setAccountName(userDetails.getAccountName());
		}
		if (userDetails.getAccountNumber() != null) {
			user.setAccountNumber(userDetails.getAccountNumber());
		}
		if (userDetails.getOrganisations() != null && !userDetails.getOrganisations().isEmpty()) {
			user.setOrganisations(userDetails.getOrganisations().stream()
					.map(o -> orgRepo.getOne(o.getId()))
					.collect(Collectors.toSet())
			);
		}

		user = userRepo.save(user);

		return UserResponse.from(user);
	}


	private void updateUserPassword(String email, String clearPassword) {
		String hash = passwordEncoder.encode(clearPassword);

		UserEntity userEntity = getUser(email);
		userEntity.setHash(hash);
		userRepo.save(userEntity);
	}


	private String composeResetCode() {
		StringBuilder code = new StringBuilder();

		int codeLength = 6;
		for (int i = 0; i < codeLength; i++)
			code.append(randomUpperCaseCharacter());

		return code.toString();
	}


	private char randomUpperCaseCharacter() {
		int salt = (int) Math.floor(new Random().nextDouble() * 26);
		return (char) (salt + (int) 'A');
	}


	private Date getExpiryTime() {
		Date now = new Date();
		Date expiryTime = new Date();
		expiryTime.setTime(now.getTime() + TimeUnit.MINUTES.toMillis(resetCodeDelayInMinutes));
		return expiryTime;
	}


	private void validateOrganisations(Set<LookupDto> organisations) {
		if (organisations == null)
			return;

		Set<UUID> organisationIds = organisations.stream()
				.map(LookupDto::getId)
				.collect(Collectors.toSet());
		validateOrganisationIds(organisationIds);
	}


	private void validateOrganisationIds(Set<UUID> organisationIds) {
		organisationIds.forEach(organisationId -> {
			if (!orgRepo.existsById(organisationId))
				ServiceUtils.throwWrongIdException("Organisation", organisationId);
		});
	}


	private ResetCodeEntity verifyCode(String email, String resetCode) {
		UserEntity userEntity = getUser(email);

		ResetCodeEntity resetCodeEntity = resetCodeRepo.findByUserIdAndResetCode(userEntity.getId(), resetCode);
		if (resetCodeEntity == null)
			throw new YSellRuntimeException("Invalid reset code");
		if (resetCodeEntity.getExpiryTimestamp().before(new Date()))
			throw new YSellRuntimeException("Reset code has expired");

		return resetCodeEntity;
	}


	private UserEntity getUser(String email) {
		return userRepo.findFirstByEmailIgnoreCase(email)
				.orElseThrow(() -> ServiceUtils.wrongEmailException("User", email));
	}
}