package com.ysell.config.jwt.service;

import com.ysell.config.jwt.models.AppUserDetails;
import com.ysell.modules.user.domain.abstractions.UserDao;
import com.ysell.modules.user.models.dto.output.UserPasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {
	
	private final UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserPasswordDto userDto = userDao.getUsernameAndPassword(username).orElse(null);
		
		if(userDto == null || !userDto.isActive())
			return new AppUserDetails(0, username, null);
		
		return new AppUserDetails(userDto.getId(), userDto.getUsername(), userDto.getHashedPassword());
	}
}
