package com.ysell.modules.common.services.impl;

import com.ysell.config.jwt.models.AppUserDetails;
import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.repositories.UserRepository;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.services.LoggedInUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author tchineke
 * @since 13 February, 2021
 */
@Service
@RequiredArgsConstructor
public class LoggedInUserServiceImpl implements LoggedInUserService {

    private final UserRepository userRepository;


    @Override
    public UserEntity getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(principal instanceof AppUserDetails))
            throw new YSellRuntimeException("User is not logged in");

        String username = ((AppUserDetails)principal).getUsername();
        return userRepository.findFirstByEmailIgnoreCase(username)
                .orElseThrow(() -> new YSellRuntimeException(String.format("User with username %s not found", username)));
    }
}
