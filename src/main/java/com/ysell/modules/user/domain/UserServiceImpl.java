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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

	@PersistenceContext
	private EntityManager entityManager;

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


	@Override
	public UserResponse createUser(CreateUserRequest request) {
		if (request.getOrganisations().size() == 0)
			throw new YSellRuntimeException("A user must belong to an organisation");

		if (userRepo.existsByEmailIgnoreCase(request.getEmail()))
			ServiceUtils.throwWrongEmailException("User", request.getEmail());

		Set<UUID> organisationIds = request.getOrganisations().stream()
				.map(LookupDto::getId)
				.collect(Collectors.toSet());
		validateOrganisationIds(organisationIds);

		String hash = passwordEncoder.encode(request.getPassword());

		return performUserCreation(request, hash);
	}


	@Override
	public UserResponse updateUser(UpdateUserRequest request) {
		userRepo.findFirstByEmailIgnoreCase(request.getEmail()).ifPresent(existingUser -> {
			if (existingUser.getId() != request.getId())
				ServiceUtils.throwWrongEmailException("User", request.getEmail());
		});

		Set<UUID> organisationIds = request.getOrganisations().stream()
				.map(LookupDto::getId)
				.collect(Collectors.toSet());
		validateOrganisationIds(organisationIds);

		return performUserUpdate(request);
	}


	@Override
	public UserResponse unsubscribe(SubscriptionRequest request) {
		UserEntity user = userRepo.findById(request.getUserId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", request.getUserId()));

		if(!user.isActive())
			throw new YSellRuntimeException(format("Account with Id %s is already unsubscribed", request.getUserId()));

		mapper.ty
		user.setActive(false);
		user = userRepo.save(user);

		return mapper.map(user, UserResponse.class);
	}


	@Override
	public UserResponse resubscribe(SubscriptionRequest request) {
		UserEntity user = entityManager.find(UserEntity.class, request.getUserId());
		if (user == null)
			ServiceUtils.throwWrongIdException("User", request.getUserId());

		if (user.isActive())
			throw new YSellRuntimeException(format("Account with Id %s is already subscribed", request.getUserId()));

		Query query = entityManager.createNativeQuery("update :tableName set is_active = 1 where id = :id");
		query.setParameter("tableName", user.getTableName());
		query.setParameter("id", request.getUserId());
		query.executeUpdate();

		return mapper.map(user, UserResponse.class);
	}


	@Override
	public SimpleMessageResponse resetCodeInitiate(ResetInitiateRequest request) {
		UserEntity userEntity = getUser(request.getEmail());

		String resetCode = getResetCode();
		Date expiryTimestamp = getExpiryTime();

		ResetCodeEntity resetCodeEntity = new ResetCodeEntity(userEntity, resetCode, expiryTimestamp);
		resetCodeRepo.save(resetCodeEntity);

		String msg = format("Your password reset code is %s. It will expire in %d minutes", resetCode, resetCodeDelayInMinutes);
		EmailModel model = new EmailModel(request.getEmail(), "YSell Password Reset Code", msg, null);

		try {
			emailSender.Send(model);
		} catch (Exception ex) {
			log.error("Error occurred while sending reset code email: ", ex);
			throw new YSellRuntimeException("Error occurred while sending reset code. Please meet administrator");
		}

		return new SimpleMessageResponse(format("Reset code has been sent to your email. It will expire in %d minutes", resetCodeDelayInMinutes));
	}


	@Override
	public SimpleMessageResponse resetCodeVerify(ResetVerifyRequest request) {
		verifyCode(request.getEmail(), request.getResetCode());
		return new SimpleMessageResponse("Valid reset code");
	}


	@Override
	public SimpleMessageResponse resetPassword(ResetPasswordRequest request) {
		ResetCodeEntity resetCodeEntity = verifyCode(request.getEmail(), request.getResetCode());
		resetCodeRepo.delete(resetCodeEntity);

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


	private UserResponse performUserCreation(CreateUserRequest userDetails, String hash) {
		UserEntity user = mapper.map(userDetails, UserEntity.class);
		user.setHash(hash);
		user = userRepo.save(user);

		UserResponse response = mapper.map(user, UserResponse.class);
		response.getOrganisations().forEach(org -> org.setName(orgRepo.getOne(org.getId()).getName()));

		return response;
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


	private String getResetCode() {
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
			throw new YSellRuntimeException("Invalid reset code", null);
		if (resetCodeEntity.getExpiryTimestamp().before(new Date()))
			throw new YSellRuntimeException("Reset code has expired", null);

		return resetCodeEntity;
	}


	private UserEntity getUser(String email) {
		return userRepo.findFirstByEmailIgnoreCase(email)
				.orElseThrow(() -> ServiceUtils.wrongEmailException("User", email));
	}
}
