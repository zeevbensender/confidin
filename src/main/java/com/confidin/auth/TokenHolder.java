package com.confidin.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by bensende on 01/05/2017.
 */
public class TokenHolder {
    public String getToken(){
        SecurityContext sContext = SecurityContextHolder.getContext();
        if(sContext != null){
            Authentication auth = sContext.getAuthentication();
            if(auth != null && auth instanceof LinkedinAuthenticationToken)
                return ((LinkedinAuthenticationToken)auth).getTokenString();
        }
        return null;

    }

}
