package com.ysell.modules.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

/**
 * @author tchineke
 * @since 13 February, 2021
 */
@AllArgsConstructor
@Getter
public class ValidateEmailRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String code;
}
