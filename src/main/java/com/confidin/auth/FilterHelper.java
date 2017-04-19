package com.confidin.auth;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bensende on 14/04/2017.
 */
public class FilterHelper {

    public String getRootPage(HttpServletRequest req){
        String uri = req.getRequestURI();
        StringBuffer url = req.getRequestURL();
        if("/".equals(uri))
            return url.toString();
        if(uri == null || uri.trim().length() == 0)
            return url.toString();
        return url.substring(0, url.indexOf(uri));
    }

}
