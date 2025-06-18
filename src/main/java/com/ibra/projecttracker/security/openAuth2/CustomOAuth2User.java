package com.ibra.projecttracker.security.openAuth2;


import com.ibra.projecttracker.security.AuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    private final AuthUser authUser;

    public CustomOAuth2User(OAuth2User oauth2User, AuthUser authUser) {
        this.oauth2User = oauth2User;
        this.authUser = authUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Use authorities from our AuthUser (which has the correct role from database)
        return authUser.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getName();
    }

    // Convenience method to get email
    public String getEmail() {
        return authUser.getUsername();
    }

    // Convenience method to get the underlying AuthUser
    public AuthUser getAuthUser() {
        return authUser;
    }
}