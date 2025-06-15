package com.ibra.projecttracker.security;

import com.ibra.projecttracker.security.jwt.JwtAuthFilter;
import com.ibra.projecttracker.security.jwt.JwtUtils;
import com.ibra.projecttracker.security.openAuth2.OAuth2LoginSuccessHandler;
import com.ibra.projecttracker.security.openAuth2.OidOAuth2UserService;
import com.ibra.projecttracker.security.openAuth2.StdOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import static org.springframework.security.config.Customizer.withDefaults;
// Duplicate imports removed for clarity
// import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
// import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final StdOAuth2UserService stdOAuth2UserService;
    private final OidOAuth2UserService oidOAuth2UserService;
    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;


    public SecurityConfig(JwtAuthFilter jwtAuthFilter, StdOAuth2UserService stdOAuth2UserService, OidOAuth2UserService oidOAuth2UserService, OAuth2LoginSuccessHandler oauth2LoginSuccessHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.stdOAuth2UserService = stdOAuth2UserService;
        this.oidOAuth2UserService = oidOAuth2UserService;
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtUtils jwtUtils) throws Exception {
        httpSecurity
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            throw authException; // Or handle with a custom response
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            throw accessDeniedException; // Or handle with a custom response
                        })
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/**", "/api/v1/home", "/oauth2/callback/**", "/login/oauth2/code/**").permitAll()
                        .requestMatchers("/admin/**", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/projects").hasAnyAuthority("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/projects/**").hasAnyAuthority("MANAGER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/tasks").hasAnyAuthority("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/task-assignments").hasAnyAuthority("MANAGER", "ADMIN")

//                        .requestMatchers(HttpMethod.PUT, "/api/v1/tasks/{id}").hasAnyAuthority("ADMIN", "DEVELOPER")

                        .requestMatchers(HttpMethod.GET, "/api/v1/projects/**").hasAnyAuthority("MANAGER", "ADMIN", "DEVELOPER", "CONTRACTOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**").hasAnyAuthority("MANAGER", "ADMIN", "DEVELOPER", "CONTRACTOR")
                        .requestMatchers("/api/v1/users/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(stdOAuth2UserService)
                                .oidcUserService(oidOAuth2UserService)
                        ).successHandler(oauth2LoginSuccessHandler)
                )

                .headers(headers -> headers
                        .frameOptions(configurer -> configurer.deny())
                        .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline'; " +
                                        "style-src 'self' 'unsafe-inline'; " +
                                        "img-src 'self' data:; " +
                                        "font-src 'self' https://cdn.scite.ai data: moz-extension:; " +
                                        "frame-ancestors 'none'"
                        ))
                        .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .permissionsPolicyHeader(permissions -> permissions.policy("camera=(), microphone=(), geolocation=()"))
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}