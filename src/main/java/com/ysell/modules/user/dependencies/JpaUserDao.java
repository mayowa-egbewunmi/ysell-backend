package com.ysell.modules.user.dependencies;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.entities.ResetCodeEntity;
import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ResetCodeRepository;
import com.ysell.jpa.repositories.UserRepository;
import com.ysell.modules.common.utilities.MapperUtils;
import com.ysell.modules.user.domain.abstractions.UserDao;
import com.ysell.modules.user.models.dto.input.CreateResetCodeDto;
import com.ysell.modules.user.models.dto.output.ActiveUserDto;
import com.ysell.modules.user.models.dto.output.ResetCodeDto;
import com.ysell.modules.user.models.dto.output.UserPasswordDto;
import com.ysell.modules.user.models.request.CreateUserRequest;
import com.ysell.modules.user.models.request.UpdateUserRequest;
import com.ysell.modules.user.models.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaUserDao implements UserDao {

	private final UserRepository userRepo;
	private final OrganisationRepository orgRepo;
	private final ResetCodeRepository resetCodeRepo;
	private final ModelMapper mapper;

	@Override
	public Optional<ActiveUserDto> getUserByUsername(String username) {
		return userRepo.findByEmailIgnoreCase(username)
				.map(user -> new ActiveUserDto(user.getEmail(), user.isActive()));
	}

	@Override
	public Optional<UserPasswordDto> getUsernameAndPassword(String username) {
		return userRepo.findByEmailIgnoreCase(username)
				.map(user -> new UserPasswordDto(user.getId(), user.getEmail(), user.getHash(), user.isActive()));
	}

	@Override
	public Optional<ActiveUserDto> getUserById(long id) {
		return userRepo.findById(id)
				.map(user -> new ActiveUserDto(user.getEmail(), user.isActive()));
	}

	@Override
	public Optional<UserResponse> getUserDetailsById(long id) {
		return userRepo.findById(id)
				.map(user -> mapper.map(user, UserResponse.class));
	}

	@Override
	public List<UserResponse> getAllUsers() {
		return MapperUtils.toStream(mapper, userRepo.findAllActive(), UserResponse.class).collect(Collectors.toList());
	}

	@Override
	public UserResponse createUser(CreateUserRequest userDetails, String hash) {
		UserEntity user = mapper.map(userDetails, UserEntity.class);
		user.setHash(hash);
		user = userRepo.save(user);

		UserResponse response = mapper.map(user, UserResponse.class);
		response.getOrganisations().forEach(org -> org.setName(orgRepo.getOne(org.getId()).getName()));

		return response;
	}

	@Override
	public UserResponse updateUser(UpdateUserRequest userDetails) {
		UserEntity oldUser = userRepo.findById(userDetails.getId()).get();

		UserEntity user = mapper.map(userDetails, UserEntity.class);
		user.setHash(oldUser.getHash());
		user = userRepo.save(user);

		return mapper.map(user, UserResponse.class);
	}

	@Override
	public UserResponse updateUserPassword(String email, String hash) {
		UserEntity user = userRepo.findByEmailIgnoreCase(email).get();
		user.setHash(hash);
		user = userRepo.save(user);

		return mapper.map(user, UserResponse.class);
	}

	@Override
	public UserResponse unsubscribeUser(long userId) {
		UserEntity user = userRepo.findById(userId).get();
		user.setActive(false);
		user = userRepo.save(user);

		return mapper.map(user, UserResponse.class);
	}

	@Override
	public UserResponse resubscribeUser(long userId) {
		UserEntity user = userRepo.findById(userId).get();
		user.setActive(true);
		user = userRepo.save(user);

		return mapper.map(user, UserResponse.class);
	}

	@Override
	public void saveResetCode(CreateResetCodeDto resetCodeDto) {
		ResetCodeEntity entity = mapper.map(resetCodeDto, ResetCodeEntity.class);
		resetCodeRepo.save(entity);
	}

	@Override
	public ResetCodeDto getResetCodeDto(long userId, String code) {
		ResetCodeEntity entity = resetCodeRepo.findByUserIdAndResetCode(userId, code);
		return entity == null ? null : mapper.map(entity, ResetCodeDto.class);
	}

	@Override
	public void deactivateResetCode(long id) {
		ResetCodeEntity entity = resetCodeRepo.findById(id).orElse(null);
		entity.setActive(false);
		resetCodeRepo.save(entity);
	}

	@Override
	public boolean hasOrganisation(long organisationId) {
		return orgRepo.findById(organisationId).orElse(null) != null;
	}

	@Override
	public List<UserResponse> getUsersByOrganisation(long organisationId) {
		OrganisationEntity orgEntity = new OrganisationEntity();
		orgEntity.setId(organisationId);
		return MapperUtils.toStream(mapper, userRepo.findByOrganisations(orgEntity), UserResponse.class).collect(Collectors.toList());
	}

}
