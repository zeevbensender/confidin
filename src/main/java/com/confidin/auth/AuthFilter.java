package com.confidin.auth;

import com.confidin.config.FilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * Created by bensende on 14/04/2017.
 */
public class AuthFilter implements Filter {
    private final static Logger LOG = LoggerFactory.getLogger(AuthFilter.class);
    private TokenValidator tokenValidator = new TokenValidator();
    private FilterHelper filterHelper = new FilterHelper();
    private AccessTokenService tokenService = new AccessTokenService();
    private String clientId;
    private String clientSecret;
    private String authorizationUri;
    private String accessTokenUri;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        clientId = filterConfig.getInitParameter(FilterConfiguration.CLIENT_ID);
        clientSecret = filterConfig.getInitParameter(FilterConfiguration.CLIENT_SECRET);
        authorizationUri = filterConfig.getInitParameter(FilterConfiguration.AUTHORIZATION_URI);
        accessTokenUri = filterConfig.getInitParameter(FilterConfiguration.ACCESS_TOKEN_URI);
        LOG.debug("Client Id is {}", clientId);
        LOG.debug("Authorization URI is {}", authorizationUri);
        LOG.debug("########## The {} is initialized", this.getClass().getName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if(req.getRequestURI().toLowerCase().startsWith("/public") || tokenValidator.validateToken(req))   {
            filterChain.doFilter(request, response);
            return;
        }
        if(tokenService.obtainAccessToken(req, accessTokenUri, clientId, clientSecret)){
            filterChain.doFilter(request, response);
            return;
        }
        resp.sendRedirect(String.format(authorizationUri, clientId, filterHelper.getRootPage(req)));
    }

    @Override
    public void destroy() {

    }
}
