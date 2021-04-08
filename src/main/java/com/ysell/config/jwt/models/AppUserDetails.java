package com.ysell.config.jwt.models;

import com.ysell.config.AuthorityConfig;
import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.entities.UserRoleEntity;
import com.ysell.jpa.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
public class AppUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final UUID userId;

	private final String username;

	private final String password;

	private final boolean enabled;

	private Collection<Role> roles;

	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}


	public static AppUserDetails from(UserEntity userEntity) {
		Set<Role> roles = userEntity.getUserRoles().stream()
				.map(UserRoleEntity::getRole)
				.collect(Collectors.toSet());

		Set<YsellAuthority> authorities = roles.stream()
				.flatMap(role -> AuthorityConfig.ROLE_AUTHORITY_MAP.getOrDefault(role, new HashSet<>()).stream())
				.map(YsellAuthority::new)
				.collect(Collectors.toSet());

		authorities.addAll(roles.stream()
				.map(role -> new YsellAuthority("ROLE_" + role))
				.collect(Collectors.toSet()));

		return AppUserDetails.builder()
				.userId(userEntity.getId())
				.username(userEntity.getEmail())
				.password(userEntity.getHash())
				.enabled(userEntity.getActivated())
				.roles(roles)
				.authorities(authorities)
				.build();
	}
}
