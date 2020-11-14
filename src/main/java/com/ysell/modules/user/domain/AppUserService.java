package com.ysell.modules.user.domain;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.common.utilities.email.EmailSender;
import com.ysell.modules.common.utilities.email.models.EmailModel;
import com.ysell.modules.user.domain.abstractions.UserDao;
import com.ysell.modules.user.domain.abstractions.UserService;
import com.ysell.modules.user.models.dto.input.CreateResetCodeDto;
import com.ysell.modules.user.models.dto.output.ActiveUserDto;
import com.ysell.modules.user.models.dto.output.ResetCodeDto;
import com.ysell.modules.user.models.dto.output.UserPasswordDto;
import com.ysell.modules.user.models.request.*;
import com.ysell.modules.user.models.response.PasswordChangeResponse;
import com.ysell.modules.user.models.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserService {

	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private final EmailSender emailSender;
	private final int resetCodeDelayInMinutes = 30;


	@Override
	public List<UserResponse> getAllUsers() {
		return userDao.getAllUsers();
	}

	@Override
	public UserResponse getUserById(long id) {
		return userDao.getUserDetailsById(id)
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", id));
	}

	@Override
	@Transactional
	public UserResponse createUser(@Valid CreateUserRequest request) {
		userDao.getUserByUsername(request.getEmail())
				.ifPresent(existingUser -> {
					String msg = existingUser.isActive() ? format("User with email %s already exists", request.getEmail()) :
							"Email linked to an unsubscribed account";
					throw new YSellRuntimeException(msg);
				});

		validateOrganisationIds(request.getOrganisations());

		String hash = passwordEncoder.encode(request.getPassword());

		return userDao.createUser(request, hash);
	}

	@Override
	@Transactional
	public UserResponse updateUser(@Valid UpdateUserRequest request) {
		ActiveUserDto existingUser = userDao.getUserById(request.getId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", request.getId()));
		if (!existingUser.isActive())
			throw new YSellRuntimeException("Id linked to an unsubscribed account");

		userDao.getUserByUsername(request.getEmail())
				.ifPresent(sameOrOtherUser -> {
					if (!sameOrOtherUser.getUsername().equals(existingUser.getUsername()))
						throw new YSellRuntimeException(format("Another user with email %s already exists", request.getEmail()));
				});

		validateOrganisationIds(request.getOrganisations());

		return userDao.updateUser(request);
	}

	private void validateOrganisationIds(Set<LookupDto> orgDtos) {
		if (orgDtos == null || orgDtos.size() == 0)
			return;

		for (LookupDto orgDto : orgDtos) {
			if (!userDao.hasOrganisation(orgDto.getId()))
				throw ServiceUtils.wrongIdException("Organisation", orgDto.getId());
		}
	}

	@Override
	@Transactional
	public UserResponse unsubscribe(@Valid SubscriptionRequest request) {
		validateSubscribingUserId(request.getUserId());
		return userDao.unsubscribeUser(request.getUserId());
	}

	@Override
	@Transactional
	public UserResponse resubscribe(@Valid SubscriptionRequest request) {
		validateSubscribingUserId(request.getUserId());
		return userDao.resubscribeUser(request.getUserId());
	}

	private void validateSubscribingUserId(long userId) {
		ActiveUserDto existingUser = userDao.getUserById(userId)
				.orElseThrow(() -> ServiceUtils.wrongIdException("User", userId));
		if (!existingUser.isActive()) {
			throw new YSellRuntimeException(format("Account with Id %s already unsubscribed", userId));
		}
	}

	@Override
	@Transactional
	public PasswordChangeResponse resetCodeInitiate(@Valid ResetInitiateRequest request) {
		UserPasswordDto existingUser = userDao.getUsernameAndPassword(request.getEmail())
				.orElseThrow(() -> new YSellRuntimeException(format("User with email %s does not exist", request.getEmail())));
		if (!existingUser.isActive()) {
			throw new YSellRuntimeException(format("Email linked to an unsubscribed account", request.getEmail()));
		}

		String resetCode = getResetCode();
		Date expiryTimestamp = getExpiryTime();
		CreateResetCodeDto resetDto = new CreateResetCodeDto(resetCode, expiryTimestamp, LookupDto.create(existingUser.getId()));
		userDao.saveResetCode(resetDto);

		String msg = format("Your password reset code is %s.\r\nIt will expire in %d minutes", resetCode, resetCodeDelayInMinutes);
		EmailModel model = new EmailModel(request.getEmail(), "YSell Password Reset Code", msg, null);

		try {
			emailSender.Send(model);
		} catch (Exception ex) {
			throw new YSellRuntimeException("An error occurred while sending reset code");
		}

		return new PasswordChangeResponse(format("Reset code has been sent to your email. It will expire in %d minutes", resetCodeDelayInMinutes));
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

	@Override
	public PasswordChangeResponse resetCodeVerify(@Valid ResetVerifyRequest request) {
		verifyCode(request.getEmail(), request.getResetCode());
		return new PasswordChangeResponse("Valid reset code");
	}

	@Override
	@Transactional
	public PasswordChangeResponse resetPassword(@Valid ResetPasswordRequest request) {
		ResetCodeDto resetCodeDto = verifyCode(request.getEmail(), request.getResetCode());
		userDao.deactivateResetCode(resetCodeDto.getId());

		String hash = passwordEncoder.encode(request.getNewPassword());
		userDao.updateUserPassword(request.getEmail(), hash);

		return new PasswordChangeResponse("Password successfully reset");
	}

	private ResetCodeDto verifyCode(String email, String resetCode) {
		UserPasswordDto existingUser = validateEmailUnique(email);

		ResetCodeDto resetCodeDto = userDao.getResetCodeDto(existingUser.getId(), resetCode);
		if (resetCodeDto == null)
			throw new YSellRuntimeException("Invalid reset code", null);
		if (!resetCodeDto.isActive())
			throw new YSellRuntimeException("Reset code has already been used", null);
		if (resetCodeDto.getExpiryTimestamp().before(new Date()))
			throw new YSellRuntimeException("Reset code has expired", null);

		return resetCodeDto;
	}

	private UserPasswordDto validateEmailUnique(String email) {
		UserPasswordDto existingUser = userDao.getUsernameAndPassword(email)
				.orElseThrow(() -> new YSellRuntimeException(format("User with email %s does not exist", email)));
		if (!existingUser.isActive()) {
			throw new YSellRuntimeException("Email linked to an unsubscribed account");
		}

		return existingUser;
	}

	@Override
	@Transactional
	public PasswordChangeResponse changePassword(@Valid ChangePasswordRequest request) {
		UserPasswordDto existingUser = validateEmailUnique(request.getEmail());

		if (!passwordEncoder.matches(request.getOldPassword(), existingUser.getHashedPassword()))
			throw new YSellRuntimeException("Old password does not match current password");

		String hash = passwordEncoder.encode(request.getNewPassword());
		userDao.updateUserPassword(request.getEmail(), hash);

		return new PasswordChangeResponse("Password successfully changed");
	}

	@Override
	@Transactional
	public List<UserResponse> getUsersByOrganisation(@Valid UsersByOrganisationRequest request) {
		validateOrganisationIds(request.getOrganisations());

		return request.getOrganisations().stream()
				.flatMap(organisation -> userDao.getUsersByOrganisation(organisation.getId()).stream())
				.collect(Collectors.toList());
	}
}
