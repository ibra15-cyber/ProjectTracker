package com.ibra.projecttracker.utility.event.auditLog;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class AuthenticationEventPublisher {

    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }
}
