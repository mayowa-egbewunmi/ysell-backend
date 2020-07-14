package com.example.ysell.service;

import com.example.ysell.Entity.UserEntity;
import com.example.ysell.repository.UserRepository;

import model.MyUserDetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService{
	
    private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

    public UserEntity createUser(final UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }
    
    public UserEntity getUserByEmail(final String email) {
    	UserEntity user = new UserEntity();
    	user.setEmail("mayowaegbewunmi@gmail.com");
    	return user;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	UserEntity user = new UserEntity();
    	user.setEmail("mayowaegbewunmi@gmail.com");
    	user.setPassword("$2a$10$A3NEr93zOQVmUPEu9uSeBOSDWOZ6UbrbNsi4UO4V2cXI9mYJFoG1S");
    	
		return new MyUserDetails(user);
	}
}
