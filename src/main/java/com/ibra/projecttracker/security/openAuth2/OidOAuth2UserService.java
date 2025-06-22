package com.ibra.projecttracker.security.openAuth2;

import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.repository.ContractorRepository;
import com.ibra.projecttracker.repository.UserRepository;
import com.ibra.projecttracker.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class OidOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {


    private final UserRepository userRepository;
    private  final JwtUtils jwtUtils;
    private final ContractorRepository contractorRepository;

    public OidOAuth2UserService(UserRepository userRepository, JwtUtils jwtUtils, ContractorRepository contractorRepository) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.contractorRepository = contractorRepository;
    }


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("Loading OIDC user from request: {}", userRequest);
        userRequest.getIdToken().getEmail();

        OidcUser oidcUser;
        if (Objects.equals(userRequest.getIdToken().getEmail(), "152512sch@gmail.com")) {
               oidcUser = new DefaultOidcUser(
                AuthorityUtils.createAuthorityList("ADMIN"),
                userRequest.getIdToken());
        } else {
            oidcUser = new DefaultOidcUser(
                AuthorityUtils.createAuthorityList("CONTRACTOR"),
                userRequest.getIdToken());
        }

        log.info("User email: {}", oidcUser.getEmail());

        String email = oidcUser.getEmail();

        String[] firstLastName = oidcUser.getFullName().split(" ");
        String firstName = firstLastName[0];

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(email, firstName));


        log.debug("User found or created: {}", user);

        String token = jwtUtils.generateToken(user);
        log.info("User logged in. Spring Security Context Authorities: {}", oidcUser.getAuthorities());

        return oidcUser;
    }

    private User registerNewUser(String email, String firstName) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName("Contractor");
        newUser.setPassword(UUID.randomUUID().toString());
        if (Objects.equals(email, "152512sch@gmail.com")) {
            newUser.setUserRole(UserRole.ADMIN);
        } else {
            newUser.setUserRole(UserRole.CONTRACTOR);
        }

        return userRepository.save(newUser);
    }
}