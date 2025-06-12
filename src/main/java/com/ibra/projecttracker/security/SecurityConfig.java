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

import static org.springframework.security.config.Customizer.withDefaults;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtUtils jwtUtils) throws  Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/home","/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/refresh-token", "/api/v1/auth/logout").permitAll()
//
//                        .requestMatchers(HttpMethod.GET, "/api/v1/projects/**").hasAuthority("CONTRACTOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**").hasAuthority("CONTRACTOR")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users/me").authenticated()
//
//                        .requestMatchers("/api/v1/projects/**").hasAnyAuthority("MANAGER", "ADMIN")
//
//                        .requestMatchers(HttpMethod.POST, "/api/v1/tasks").hasAnyAuthority("MANAGER", "ADMIN")
//
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tasks/{id}").hasAnyAuthority("MANAGER", "ADMIN", "DEVELOPER")
//
//                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // General admin paths
//
//                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(stdOAuth2UserService)
                                .oidcUserService(oidOAuth2UserService)
                        ).successHandler(oauth2LoginSuccessHandler)
                );

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

