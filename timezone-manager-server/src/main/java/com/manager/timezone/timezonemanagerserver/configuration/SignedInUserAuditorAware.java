package com.manager.timezone.timezonemanagerserver.configuration;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SignedInUserAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("system");
        } else {
            if (authentication.getPrincipal() instanceof AuthUserDetails) {
                return Optional.of(((AuthUserDetails) authentication.getPrincipal()).getUsername());
            } else {
                return Optional.of("system");
            }
        }
    }
}
