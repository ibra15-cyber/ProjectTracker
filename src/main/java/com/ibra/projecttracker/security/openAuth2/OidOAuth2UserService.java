package com.ibra.projecttracker.security.openAuth2;

import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OidOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {


    private final UserRepository userRepository;

    public OidOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser oidcUser = new DefaultOidcUser(
                AuthorityUtils.createAuthorityList("ROLE_CONTRACTOR"),
                userRequest.getIdToken());

        log.debug("oidcUser .....................{}", oidcUser.toString());
        System.out.println("oidUser......" +oidcUser);

        String email = oidcUser.getEmail();

        String[] firstLastName = oidcUser.getFullName().split(" ");
        String firstName = firstLastName[0];

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(email, firstName));

        log.debug("User found or created: {}", user);

        return oidcUser;
    }

    private User registerNewUser(String email, String firstName) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setUserRole(UserRole.CONTRACTOR);

        return userRepository.save(newUser);
    }
}