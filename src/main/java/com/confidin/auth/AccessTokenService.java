package com.confidin.auth;

import com.confidin.api.ApiCallResult;
import com.confidin.api.LinkedinClient;
import com.confidin.config.FilterConfiguration;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bensende on 16/04/2017.
 */
public class AccessTokenService {
    private final static Logger LOG = LoggerFactory.getLogger(AccessTokenService.class);
//    todo: spring initialization
    private FilterHelper filterHelper = new FilterHelper();
//    todo: spring initialization
    private LinkedinClient client = new LinkedinClient();
    ObjectMapper mapper = new ObjectMapper();
    public String buildAccessTokenRequest(String base, String code, String redirect, String clientId, String clientSecret) {
        String path = String.format("%s?grant_type=authorization_code&code=%s&redirect_uri=%s&client_id=%s&client_secret=%s",
                base, code, redirect, clientId, clientSecret);
        return path;
    }

    public boolean obtainAccessToken(HttpServletRequest req, String accessTokenUri, String clientId, String clientSecret){
        String code = req.getParameter("code");
        if(code == null || code.length() == 0)
            return false;

        String requestPath = buildAccessTokenRequest(accessTokenUri, code, filterHelper.getRootPage(req), clientId, clientSecret);
        AccessToken token = obtainAccessToken(requestPath);
        if(token != null) {
            req.getSession().setAttribute(FilterConfiguration.ACCESS_TOKEN, token);
            LOG.info(">>>>>>>>>>>>>>>>>>>>>> token >>>>>>>>>>>> " + token);
            GrantedAuthority authority = new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "ROLE_ADMIN";
                }
            };
            List<GrantedAuthority> auths = new ArrayList<>();
            auths.add(authority);
            SecurityContextHolder.getContext().
                    setAuthentication(new LinkedinAuthenticationToken(auths, token));
            return true;
        }
        return false;
    }

    public AccessToken obtainAccessToken(String requestPath){
        try {
            ApiCallResult response = client.obtainToken(requestPath);
            if(response == null)
                return null;

            AccessToken token = mapper.readValue(response.getResponse() , AccessToken.class);
            token.setToken(response.getResponse());
            return token;


        } catch (IOException e) {
            throw new RuntimeException("Failed to obtain access token", e);
        }

    }
}
