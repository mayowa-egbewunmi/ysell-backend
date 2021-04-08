package com.ysell.config.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author tchineke
 * @since 07 April, 2021
 */
@AllArgsConstructor
@Getter
public class YsellAuthority implements GrantedAuthority {

    private String authority;
}
