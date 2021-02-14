package com.ysell.modules.user.models.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author tchineke
 * @since 13 February, 2021
 */
@Getter
@Setter
public class ValidateEmailRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String token;
}
