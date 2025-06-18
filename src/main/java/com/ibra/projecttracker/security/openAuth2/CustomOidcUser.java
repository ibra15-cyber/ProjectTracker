package com.ibra.projecttracker.security.openAuth2;


import com.ibra.projecttracker.security.AuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

public class CustomOidcUser implements OidcUser {

    private final OidcUser oidcUser;
    private final AuthUser authUser;

    public CustomOidcUser(OidcUser oidcUser, AuthUser authUser) {
        this.oidcUser = oidcUser;
        this.authUser = authUser;
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Use authorities from our AuthUser (which has the correct role from database)
        return authUser.getAuthorities();
    }

    @Override
    public String getName() {
        return oidcUser.getName();
    }

    // Convenience methods
    public String getEmail() {
        return authUser.getUsername();
    }

    public AuthUser getAuthUser() {
        return authUser;
    }
}