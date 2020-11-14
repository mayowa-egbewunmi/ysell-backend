package com.ysell.modules.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ysell.common.MockEmailSender;
import com.ysell.modules.user.domain.AppUserService;
import com.ysell.modules.user.domain.abstractions.UserDao;
import com.ysell.modules.user.domain.abstractions.UserService;
import com.ysell.modules.user.models.request.CreateUserRequest;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
	
	private UserService svc;
	private UserDao userDao;

	@BeforeEach
	void initUseCase() {
		userDao = new MockUserDao();
		svc = new AppUserService(userDao, new BCryptPasswordEncoder(), new MockEmailSender());
	}
	
	@Test
	void shouldContainErrorWhenSelectedUserIdDoesNotExist() {
		svc.createUser(new CreateUserRequest());
	}
		
}
