package com.ysell.config.jwt.service;

import com.ysell.config.jwt.models.AppUserDetails;
import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.repositories.UserRepository;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {
	
	private final UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepo.findFirstByEmailIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));

		if(!userEntity.getActivated())
			throw new YSellRuntimeException(String.format("%s is disabled. Please meet with administrator", username));
		
		return new AppUserDetails(userEntity.getId(), userEntity.getEmail(), userEntity.getHash());
	}
}
