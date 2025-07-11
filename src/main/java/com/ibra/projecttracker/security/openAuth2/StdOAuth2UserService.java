package com.ibra.projecttracker.security.openAuth2;

import com.ibra.projecttracker.entity.AuditLog;
import com.ibra.projecttracker.entity.Contractor;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.repository.ContractorRepository;
import com.ibra.projecttracker.repository.UserRepository;
import com.ibra.projecttracker.service.impl.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class StdOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public StdOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        log.info("open oauth2 standard user {}", oauth2User.toString());

        String accessToken = userRequest.getAccessToken().getTokenValue();
        log.info("open oauth2 standard access token {}", accessToken);

        String email = oauth2User.getAttribute("email");
        String username = oauth2User.getAttribute("login");

        if (email == null || email.isEmpty()) {
            email = username;
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createUser(oauth2User));
        log.info("User found or created: " + user);


        return oauth2User;
    }

    private User createUser(OAuth2User oauth2User) {
        User user = new User();
        user.setEmail(oauth2User.getAttribute("login") + "@github.com");
        user.setFirstName(oauth2User.getAttribute("login"));
        user.setLastName(oauth2User.getAttribute("login"));
        user.setUserRole(UserRole.CONTRACTOR);
        user.setPassword(UUID.randomUUID().toString());

        log.info("user found or created: {}", user);
        return userRepository.save(user);
    }
}
