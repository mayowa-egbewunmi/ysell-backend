package com.ysell.modules.common.services;

import com.ysell.jpa.entities.UserEntity;

/**
 * @author tchineke
 * @since 13 February, 2021
 */
public interface LoggedInUserService {

    UserEntity getLoggedInUser();
}
