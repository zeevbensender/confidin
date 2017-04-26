package com.confidin.auth;

import com.confidin.config.FilterConfiguration;
import com.confidin.util.CookieHelper;
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
    private SkipFilter skipFilter;
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
        if(skipFilter.skipFilter(req.getRequestURI())){
            LOG.debug("The servlet URI is {}. Skipping auth filter.", req.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        LOG.debug("\nInside doFilter. \nRequest URI: {}. \nSession ID: {}. \nReferrer: {}. \nAccess Token: {}",
                req.getRequestURI(),
                CookieHelper.getCookie("JSESSIONID", req),
                req.getHeader("referer"),
                req.getSession().getAttribute(FilterConfiguration.ACCESS_TOKEN)
                );
        if(tokenValidator.validateToken(req)){
            LOG.debug("Token was found in the session. Proceeds to the next filter in the chain.");
            filterChain.doFilter(request, response);
            return;
        }
        if(tokenService.obtainAccessToken(req, accessTokenUri, clientId, clientSecret)){
            LOG.debug("Successfully obtained token from LinkedIn. Proceeds to the next filter in the chain.");
            filterChain.doFilter(request, response);
            return;
        }
        resp.sendRedirect(String.format(authorizationUri, clientId, filterHelper.getRootPage(req)));
    }

    @Override
    public void destroy() {

    }

    public interface SkipFilter {
        boolean skipFilter(String contextPath);
    }

    public void setSkipFilter(SkipFilter skipFilter) {
        this.skipFilter = skipFilter;
    }
}
