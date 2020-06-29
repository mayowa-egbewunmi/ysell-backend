package com.example.ysell.service;

import com.example.ysell.Entities.UserEntity;
import com.example.ysell.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {
	
    private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

    public UserEntity createUser(final UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }
    
    public UserEntity getUserByEmail(final Long id) {
    	UserEntity user = new UserEntity();
    	user.setEmail("mayowaegbewunmi@gmail.com");
    	return userRepository.findById(id).orElse(user);
    }
}
