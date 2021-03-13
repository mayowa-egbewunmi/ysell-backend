package com.ysell.modules.user.domain;

import com.ysell.config.jwt.models.AppUserDetails;
import com.ysell.config.jwt.service.JwtTokenUtil;
import com.ysell.jpa.entities.ResetCodeEntity;
import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ResetCodeRepository;
import com.ysell.jpa.repositories.UserRepository;
import com.ysell.modules.common.dto.LookupDto;
import com.ysell.modules.common.dto.PageWrapper;
import com.ysell.modules.common.dto.response.SimpleMessageResponse;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.common.utilities.email.EmailSender;
import com.ysell.modules.common.utilities.email.models.EmailModel;
import com.ysell.modules.user.models.request.*;
import com.ysell.modules.user.models.response.UserRegistrationResponse;
import com.ysell.modules.user.models.response.UserResponse;
import com.ysell.modules.user.models.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

	private final ModelMapper mapper = new ModelMapper();

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
	public PageWrapper<UserResponse> getAllPaged(Pageable page) {
		return PageWrapper.from(
				userRepo.findAll(page)
						.map(userEntity -> mapper.map(userEntity, UserResponse.class))
		);
	}


	@Override
	public UserResponse getById(UUID userId) {
		return userRepo.findById(userId)
				.map(user -> mapper.map(user, UserResponse.class))
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", userId));
	}


	@Override
	public List<UserResponse> getUsersByOrganisation(Set<UUID> organisationIds) {
		validateOrganisationIds(organisationIds);

		return organisationIds.stream()
				.flatMap(organisationId -> userRepo.findByOrganisationsId(organisationId).stream())
				.map(user -> mapper.map(user, UserResponse.class))
				.collect(Collectors.toList());
	}


	//todo: refactor my validates to one line, total refactor of model mapper usages
	@Override
	public UserRegistrationResponse registerUser(CreateUserRequest request) {
		if (userRepo.existsByEmailIgnoreCase(request.getEmail()))
			ServiceUtils.throwWrongEmailException("User", request.getEmail());
		if (userRepo.existsByNameIgnoreCase(request.getName()))
			ServiceUtils.throwWrongNameException("User", request.getName());

		validateOrganisations(request.getOrganisations());

		String hash = passwordEncoder.encode(request.getPassword());

		UserEntity userEntity = performUserCreation(request, hash);

		String resetCode = generateResetCode(userEntity);
		String msg = format("Your validation code is: %s. It expires in %d minutes", resetCode, resetCodeDelayInMinutes);
		EmailModel emailModel = new EmailModel(request.getEmail(), "Ysell Email Validation", msg, null);
		emailSender.send(emailModel, "user validation code");

		unsubscribe(new SubscriptionRequest(userEntity.getId()));

		final String token = jwtTokenUtil.generateToken(userEntity.getEmail(), userEntity.getId());

		return new UserRegistrationResponse(
				"Validation code has been sent to your email - " + request.getEmail(), token, false
		);
	}


	@Override
	public UserResponse completeRegistration(ValidateEmailRequest request) {
		ResetCodeEntity resetCodeEntity = verifyCode(request.getEmail(), request.getCode());
		resetCodeRepo.deleteByUserId(resetCodeEntity.getUser().getId());
		UserEntity userEntity = getUser(request.getEmail());

		return resubscribe(new SubscriptionRequest(userEntity.getId()));
	}


	@Override
	public UserResponse updateUser(UpdateUserRequest request) {
		userRepo.findFirstByEmailIgnoreCase(request.getEmail()).ifPresent(existingUser -> {
			if (existingUser.getId() != request.getId())
				ServiceUtils.throwWrongEmailException("User", request.getEmail());
		});
		userRepo.findFirstByNameIgnoreCase(request.getName()).ifPresent(existingUser -> {
			if (existingUser.getId() != request.getId())
				ServiceUtils.throwWrongNameException("User", request.getName());
		});

		validateOrganisations(request.getOrganisations());

		return performUserUpdate(request);
	}


	@Override
	public UserResponse unsubscribe(SubscriptionRequest request) {
		UserEntity user = userRepo.findById(request.getUserId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", request.getUserId()));

		if (!user.getActivated())
			throw new YSellRuntimeException(format("Account with Id %s is already unsubscribed", request.getUserId()));

		user.setActivated(false);
		user = userRepo.save(user);

		return mapper.map(user, UserResponse.class);
	}


	@Override
	public UserResponse resubscribe(SubscriptionRequest request) {
		UserEntity user = userRepo.findById(request.getUserId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", request.getUserId()));

		if (user.getActivated())
			throw new YSellRuntimeException(format("Account with Id %s is already subscribed", request.getUserId()));

		user.setActivated(true);
		user = userRepo.save(user);

		return mapper.map(user, UserResponse.class);
	}


	@Override
	public SimpleMessageResponse resetCodeInitiate(InitiateResetPasswordRequest request) {
		UserEntity userEntity = getUser(request.getEmail());

		String resetCode = generateResetCode(userEntity);

		String msg = format("Your password reset code is %s. It will expire in %d minutes", resetCode, resetCodeDelayInMinutes);
		EmailModel emailModel = new EmailModel(request.getEmail(), "YSell Password Reset Code", msg, null);
		emailSender.send(emailModel, "reset code");

		return new SimpleMessageResponse(format("Reset code has been sent to your email. It will expire in %d minutes", resetCodeDelayInMinutes));
	}


	@Override
	public SimpleMessageResponse resetCodeVerify(ResetCodeValidateRequest request) {
		verifyCode(request.getEmail(), request.getResetCode());
		return new SimpleMessageResponse("Valid reset code");
	}


	@Override
	public SimpleMessageResponse resetPassword(ResetPasswordRequest request) {
		ResetCodeEntity resetCodeEntity = verifyCode(request.getEmail(), request.getResetCode());
		resetCodeRepo.deleteByUserId(resetCodeEntity.getUser().getId());

		updateUserPassword(request.getNewPassword(), request.getEmail());

		return new SimpleMessageResponse("Password successfully reset");
	}


	@Override
	public SimpleMessageResponse changePassword(ChangePasswordRequest request) {
		UserEntity userEntity = getUser(request.getEmail());

		if (!passwordEncoder.matches(request.getOldPassword(), userEntity.getHash()))
			throw new YSellRuntimeException("Old password does not match current password");

		updateUserPassword(request.getNewPassword(), request.getEmail());

		return new SimpleMessageResponse("Password successfully changed");
	}


	private String generateResetCode(UserEntity userEntity) {
		String resetCode = composeResetCode();

		ResetCodeEntity resetCodeEntity = new ResetCodeEntity(userEntity, resetCode, getExpiryTime());
		resetCodeRepo.save(resetCodeEntity);

		return resetCode;
	}


	private UserEntity performUserCreation(CreateUserRequest userDetails, String hash) {
		UserEntity user = mapper.map(userDetails, UserEntity.class);
		user.setHash(hash);
		return userRepo.save(user);
	}


	private UserResponse performUserUpdate(UpdateUserRequest userDetails) {
		UserEntity user = userRepo.findById(userDetails.getId()).get();

		mapper.map(userDetails, user);
		user = userRepo.save(user);

		return mapper.map(user, UserResponse.class);
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