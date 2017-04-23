package com.confidin.auth;

import com.confidin.api.ApiCallResult;
import com.confidin.api.LinkedinClient;
import com.confidin.config.FilterConfiguration;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

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
    public URL buildAccessTokenUrl(String base, String code, String redirect, String clientId, String clientSecret) {
        String path = String.format("%s?grant_type=authorization_code&code=%s&redirect_uri=%s&client_id=%s&client_secret=%s",
                base, code, redirect, clientId, clientSecret);
        try {
            return new URL(path);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to build access token request URL", e);
        }
    }

    public boolean obtainAccessToken(HttpServletRequest req, String accessTokenUri, String clientId, String clientSecret){
        String code = req.getParameter("code");
        if(code == null || code.length() == 0)
            return false;
        URL url = buildAccessTokenUrl(accessTokenUri, code, filterHelper.getRootPage(req), clientId, clientSecret);
        AccessToken token = obtainAccessToken(url);
        if(token != null) {
            req.getSession().setAttribute(FilterConfiguration.ACCESS_TOKEN, token);
            LOG.info(">>>>>>>>>>>>>>>>>>>>>> token >>>>>>>>>>>> " + token);
            return true;
        }
        return false;
    }

    public AccessToken obtainAccessToken(URL url){
        try {
            ApiCallResult response = client.invokeApi(url, "Access token");
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
