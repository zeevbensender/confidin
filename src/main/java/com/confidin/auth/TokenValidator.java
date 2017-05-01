package com.confidin.auth;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by bensende on 14/04/2017.
 */
public class TokenValidator {
    //    todo: spring initialization
    private TokenHolder tokenHolder = new TokenHolder();
    public boolean validateToken(HttpServletRequest req){

        return Optional.ofNullable(tokenHolder.getToken()).
                map(t -> t != null).
                orElse(false);
    }
}
