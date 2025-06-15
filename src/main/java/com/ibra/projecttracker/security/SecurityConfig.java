package com.ibra.projecttracker.security;

import com.ibra.projecttracker.security.jwt.JwtAuthFilter;
import com.ibra.projecttracker.security.jwt.JwtUtils;
import com.ibra.projecttracker.security.openAuth2.OAuth2LoginSuccessHandler;
import com.ibra.projecttracker.security.openAuth2.OidOAuth2UserService;
import com.ibra.projecttracker.security.openAuth2.StdOAuth2UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
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
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final StdOAuth2UserService stdOAuth2UserService;
    private final OidOAuth2UserService oidOAuth2UserService;
    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final ApplicationContext applicationContext; // <--- INJECT APPLICATION CONTEXT



    public SecurityConfig(JwtAuthFilter jwtAuthFilter, StdOAuth2UserService stdOAuth2UserService, OidOAuth2UserService oidOAuth2UserService, OAuth2LoginSuccessHandler oauth2LoginSuccessHandler, ApplicationContext applicationContext) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.stdOAuth2UserService = stdOAuth2UserService;
        this.oidOAuth2UserService = oidOAuth2UserService;
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
        this.applicationContext = applicationContext;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtUtils jwtUtils) throws Exception {
        httpSecurity
//                .exceptionHandling(exceptions -> exceptions
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            throw authException;
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            throw accessDeniedException;
//                        }))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults()).authorizeHttpRequests(request ->
                        request.requestMatchers("/api/v1/auth/oauth2/success", "/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/refresh", "/api/v1/auth/logout", "/oauth2/authorization/**", "/oauth2/callback/**", "/login/oauth2/code/**").permitAll()
                                .requestMatchers("/api/v1/projects/**", "/api/v1/tasks/**", "/api/v1/users/**", "api/v1/task-assignments/**").authenticated()

                                .requestMatchers("/api/v1/tasks/**").authenticated()
//                                .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**").hasAuthority("CONTRACTOR")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/users/me").authenticated()
////\.requestMatchers("/api/v1/projects/**").hasAnyAuthority("MANAGER", "ADMIN")
//                                .requestMatchers(HttpMethod.POST, "/api/v1/projects").hasAnyAuthority("MANAGER", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/projects/**").hasAnyAuthority("MANAGER", "ADMIN")

//                                .requestMatchers(HttpMethod.POST, "/api/v1/tasks").hasAnyAuthority("MANAGER", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/api/v1/tasks/{id}").hasAnyAuthority( "ADMIN", "DEVELOPER")
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/tasks/{id}").hasAnyAuthority( "ADMIN", "DEVELOPER")
//////
//                                .requestMatchers("/api/tasks/**").hasAuthority("ADMIN")
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated())
//                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(stdOAuth2UserService).oidcUserService(oidOAuth2UserService)).successHandler(oauth2LoginSuccessHandler))

                .headers(headers -> headers
                        // Frame Options (X-Frame-Options)
                        .frameOptions(configurer -> configurer.deny())
                        // XSS Protection (X-XSS-Protection)
                        .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        // Content Security Policy (CSP) - CORRECTED BACK TO policyDirectives
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline'; " +
                                        "style-src 'self' 'unsafe-inline'; " + // ADDED: Allows inline styles
                                        "img-src 'self' data:; " +             // ADDED: Allows data: URIs for images
                                        "font-src 'self' https://cdn.scite.ai data: moz-extension:; " +
                                        "frame-ancestors 'none'"
                        ))
                        // Referrer Policy
                        .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .permissionsPolicy(permissions -> permissions.policy("camera=(), microphone=(), geolocation=()")));

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

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }
}