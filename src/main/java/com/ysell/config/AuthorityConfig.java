package com.ysell.config;

import com.ysell.jpa.entities.enums.Role;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tchineke
 * @since 07 April, 2021
 */
@Service
public class AuthorityConfig {

    public static final String SOFT_DELETE = "user.softDelete";
    public static final String UNDELETE = "user.undelete";

    public static final Map<Role, Set<String>> ROLE_AUTHORITY_MAP;

    static {
        ROLE_AUTHORITY_MAP = Collections.singletonMap(
                Role.ADMIN,
                Stream.of(
                        SOFT_DELETE,
                        UNDELETE
                ).collect(Collectors.toSet())
        );
    }
}