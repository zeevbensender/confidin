package com.confidin.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by bensende on 23/04/2017.
 */
public class LinkedinAuthenticationToken extends AbstractAuthenticationToken {
    private AccessToken accessToken;
    private AuthenticatedPrincipal principal;
    public LinkedinAuthenticationToken(Collection<? extends GrantedAuthority> authorities, AccessToken accessToken) {
        super(authorities);
        super.setAuthenticated(true);
        setDetails(accessToken);
        this.accessToken = accessToken;
    }

    public void setPrincipal(AuthenticatedPrincipal principal) {
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public String getTokenString(){
        return Optional.ofNullable(accessToken).map(AccessToken::getValue).orElse(null);
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
