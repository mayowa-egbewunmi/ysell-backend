package com.ysell.jpa.auditor;

import com.ysell.common.constants.AppConstants;
import com.ysell.config.jwt.models.AppUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@EnableJpaAuditing
public class AppAuditorAware implements AuditorAware<UUID> {

	@Override
	public Optional<UUID> getCurrentAuditor() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!(principal instanceof AppUserDetails))
			return Optional.of(AppConstants.SYSTEM_ID);

		return Optional.of((AppUserDetails) principal)
				.map(AppUserDetails::getUserId);
	}
}