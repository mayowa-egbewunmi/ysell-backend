package com.ysell.config.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ysell.config.jwt.service.JwtTokenUtil;
import com.ysell.config.jwt.service.JwtUserDetailService;
import com.ysell.modules.user.domain.abstractions.UserDao;

@Configuration
public class JwtDependenciesRegistration {
		
	@Bean
	public UserDetailsService userDetails(UserDao userDao) {
		return new JwtUserDetailService(userDao);
	}

	@Bean
	public JwtTokenUtil jwtTokenUtil() {
		return new JwtTokenUtil();
	}
}
