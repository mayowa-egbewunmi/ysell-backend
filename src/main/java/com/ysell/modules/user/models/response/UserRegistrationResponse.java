package com.ysell.modules.user.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tchineke
 * @since 14 February, 2021
 */
@AllArgsConstructor
@Getter
public class UserRegistrationResponse {

    private String message;

    private String token;

    private boolean activated;
}
