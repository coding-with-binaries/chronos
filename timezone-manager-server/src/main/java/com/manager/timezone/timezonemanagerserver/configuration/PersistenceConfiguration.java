package com.manager.timezone.timezonemanagerserver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class PersistenceConfiguration {
    @Bean
    AuditorAware<String> auditorProvider() {
        return new SignedInUserAuditorAware();
    }
}
