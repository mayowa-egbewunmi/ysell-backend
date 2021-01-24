package com.ysell.jpa.auditor;

import com.ysell.config.jwt.models.AppUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@EnableJpaAuditing
public class AppAuditorAware implements AuditorAware<UUID> {

	@Override
	public Optional<UUID> getCurrentAuditor() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AppUserDetails userDetails = obj instanceof AppUserDetails ? (AppUserDetails) obj : null;

		return Optional.ofNullable(userDetails)
				.map(AppUserDetails::getUserId);
	}
}
