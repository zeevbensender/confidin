package com.confidin.auth;


import org.springframework.security.core.AuthenticatedPrincipal;

import java.security.Principal;

/**
 * Created by bensende on 28/04/2017.
 */
public class LinkedinPrincipal implements AuthenticatedPrincipal, Principal {
    AccessToken token;

    public LinkedinPrincipal(AccessToken token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return "Zeev";
    }
}
