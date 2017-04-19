package com.confidin.auth;

import com.confidin.config.FilterConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by bensende on 14/04/2017.
 */
public class TokenValidator {
    public boolean validateToken(HttpServletRequest req){

        return Optional.ofNullable(req.getSession(true).getAttribute(FilterConfiguration.ACCESS_TOKEN)).
                map(t -> !((String)t).isEmpty()).
                orElse(false);
    }
}
