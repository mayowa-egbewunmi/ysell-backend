package com.ysell.modules.user.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ResetCodeRepository;
import com.ysell.jpa.repositories.UserRepository;
import com.ysell.modules.common.utilities.email.EmailSender;
import com.ysell.modules.user.dependencies.JpaUserDao;
import com.ysell.modules.user.domain.AppUserService;
import com.ysell.modules.user.domain.abstractions.UserDao;
import com.ysell.modules.user.domain.abstractions.UserService;

@Configuration
public class UserDependenciesRegistration {

	@Bean
	public UserDao userDao(UserRepository userRepo, OrganisationRepository orgRepo, ResetCodeRepository resetCodeRepo, ModelMapper modelMapper) {
		return new JpaUserDao(userRepo, orgRepo, resetCodeRepo, modelMapper);
	}
	
	@Bean
	public UserService userService(UserDao userDao, PasswordEncoder passwordEncoder, EmailSender emailSender) {
		return new AppUserService(userDao, passwordEncoder, emailSender);
	}
}
