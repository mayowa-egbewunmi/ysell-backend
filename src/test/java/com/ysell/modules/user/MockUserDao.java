package com.ysell.modules.user;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import com.ysell.jpa.entities.UserEntity;
import com.ysell.modules.user.domain.abstractions.UserDao;
import com.ysell.modules.user.models.dto.input.CreateResetCodeDto;
import com.ysell.modules.user.models.dto.output.ActiveUserDto;
import com.ysell.modules.user.models.dto.output.ResetCodeDto;
import com.ysell.modules.user.models.dto.output.UserPasswordDto;
import com.ysell.modules.user.models.request.CreateUserRequest;
import com.ysell.modules.user.models.request.UpdateUserRequest;
import com.ysell.modules.user.models.response.UserResponse;

public class MockUserDao implements UserDao {

	public List<UserEntity> users;

	private ModelMapper mapper = new ModelMapper();

	public MockUserDao() {
		UserEntity user1 = new UserEntity("chineketobenna@gmail.com", "Tobenna", "$2a$10$4a0pl5Twxk4spAux0oN/4ezUxm4XopO./00/t55j6LC93bHZejExu", null, true);
		user1.setId(1);
		UserEntity user2 = new UserEntity("mayowaegbewunmi@gmail.com", "Mayor", "$2a$10$4a0pl5Twxk4spAux0oN/4ezUxm4XopO./00/t55j6LC93bHZejExu", null, true);
		user2.setId(2);
		UserEntity user3 = new UserEntity("john@gmail.com", "john", "$2a$10$4a0pl5Twxk4spAux0oN/4ezUxm4XopO./00/t55j6LC93bHZejExu", null, true);
		user3.setId(3);

		users = Arrays.asList(user1, user2, user3);
	}

	@Override
	public Optional<ActiveUserDto> getUserByUsername(String username) {
		return users.stream()
				.filter((r) -> r.getEmail().equals(username))
				.findAny()
				.map(user -> new ActiveUserDto(username, user.isActive()));
	}

	@Override
	public Optional<UserPasswordDto> getUsernameAndPassword(String username) {
		return users.stream()
				.filter((r) -> r.getEmail().equals(username))
				.findAny()
				.map(user -> new UserPasswordDto(user.getId(), user.getEmail(), user.getHash(), user.isActive()));
	}

	@Override
	public Optional<ActiveUserDto> getUserById(long id) {
		return users.stream()
				.filter((r) -> r.getId() == id)
				.findAny()
				.map(user -> new ActiveUserDto(user.getEmail(), user.isActive()));
	}

	@Override
	public Optional<UserResponse> getUserDetailsById(long id) {
		return users.stream()
				.filter((r) -> r.getId() == id)
				.findAny()
				.map(user -> mapper.map(user, UserResponse.class));
	}

	@Override
	public UserResponse createUser(CreateUserRequest userDetails, String hash) {
		UserEntity user = mapper.map(userDetails, UserEntity.class);
		user.setId(users.size() + 1);
		users.add(user);
		return mapper.map(user, UserResponse.class);
	}

	@Override
	public UserResponse updateUser(UpdateUserRequest userDetails) {
		UserEntity user = users.stream().filter((r) -> r.getId() == userDetails.getId()).findAny().orElseGet(null);
		int index = users.indexOf(user);

		user = mapper.map(userDetails, UserEntity.class);
		users.set(index, user);

		return mapper.map(user, UserResponse.class);
	}

	@Override
	public UserResponse updateUserPassword(String email, String hash) {
		UserEntity user = users.stream().filter((r) -> r.getEmail().equals(email)).findAny().orElseGet(null);
		int index = users.indexOf(user);
		user.setHash(hash);
		users.set(index, user);

		return mapper.map(user, UserResponse.class);
	}

	@Override
	public UserResponse unsubscribeUser(long userId) {
		UserEntity user = users.stream().filter((r) -> r.getId() == userId).findAny().orElseGet(null);
		int index = users.indexOf(user);
		user.setActive(false);
		users.set(index, user);

		return mapper.map(user, UserResponse.class);
	}

	@Override
	public UserResponse resubscribeUser(long userId) {
		UserEntity user = users.stream().filter((r) -> r.getId() == userId).findAny().orElseGet(null);
		int index = users.indexOf(user);
		user.setActive(true);
		users.set(index, user);

		return mapper.map(user, UserResponse.class);
	}

	@Override
	public void saveResetCode(CreateResetCodeDto resetCodeDto) {
		// TODO Auto-generated method stub

	}

	@Override
	public ResetCodeDto getResetCodeDto(long userId, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deactivateResetCode(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<UserResponse> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasOrganisation(long organisationId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UserResponse> getUsersByOrganisation(long organisationId) {
		// TODO Auto-generated method stub
		return null;
	}

}
