package com.confidin.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by bensende on 24/04/2017.
 */
public class CookieHelper {
    public static String getCookie(String name, HttpServletRequest req){
        Cookie[] coo = req.getCookies();
        if(coo == null)
            return null;
        String value = Arrays.stream(coo).
                filter(cnam -> cnam.getName().equals(name)).
                findFirst().map(Cookie::getName).orElse(null);
        return value;
    }
}