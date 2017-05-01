package com.confidin.auth;


import com.confidin.model.UserProfile;
import org.springframework.security.core.AuthenticatedPrincipal;

import java.security.Principal;

/**
 * Created by bensende on 28/04/2017.
 */
public class LinkedinPrincipal implements AuthenticatedPrincipal, Principal {
    private UserProfile profile;

    public LinkedinPrincipal(UserProfile profile) {
        this.profile = profile;
    }

    @Override
    public String getName() {
        return profile.getFirstName() + " " + profile.getLastName();
    }
}
