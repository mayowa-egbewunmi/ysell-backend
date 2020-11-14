package com.ysell.jpa.auditor;

import com.ysell.config.jwt.models.AppUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableJpaAuditing
public class AppAuditorAware implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AppUserDetails userDetails = obj instanceof AppUserDetails ? (AppUserDetails) obj : null;
		Long userId = userDetails == null ? 0 : userDetails.getUserId();

		return Optional.of(userId);
	}
}
