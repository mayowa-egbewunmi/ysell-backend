package com.ysell.modules.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author tchineke
 * @since 13 February, 2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ValidateEmailRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String code;
}
