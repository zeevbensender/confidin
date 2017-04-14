package com.confidin.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by bensende on 14/04/2017.
 */
public class TokenValidator {
    public boolean validateToken(HttpServletRequest req){
        String accessToken = Arrays.stream(Optional.ofNullable(req.getCookies()).map(cc -> cc).orElse(new Cookie[]{})).
                filter(coo -> coo.getName().equals("access_token")).findFirst().map(Cookie::getValue).orElse(null);
        if(accessToken != null){
//            todo: add actual token validation
            return true;
        }
        return false;
    }
}
