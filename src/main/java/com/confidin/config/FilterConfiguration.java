package com.confidin.config;

import com.confidin.auth.AuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * Created by bensende on 14/04/2017.
 */
@Configuration
public class FilterConfiguration {

    @Value("${clientSecret}")
    private String clientSecret;
    @Value("${clientId}")
    private String clientId;
    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;
    @Value("${security.oauth2.client.userAuthorizationUri}")
    private String userAuthorizationUri;
    public static final String CLIENT_SECRET = "CLIENT_SECRET";



    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String AUTHORIZATION_URI = "AUTHORIZATION_URI";
    public static final String ACCESS_TOKEN_URI = "ACCESS_TOKEN_URI";

    @Bean
    public FilterRegistrationBean authFilterRegistration() {

        FilterRegistrationBean  registration = new FilterRegistrationBean ();
        registration.setFilter(oauth2Filter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter(CLIENT_SECRET, clientSecret);
        registration.addInitParameter(CLIENT_ID, clientId);
        registration.addInitParameter(AUTHORIZATION_URI, userAuthorizationUri);
        registration.addInitParameter(ACCESS_TOKEN_URI, accessTokenUri);
        registration.setName("oauth2Filter");
        registration.setOrder(1);
        return registration;
    }

    @Bean(name = "authFilter")
    public Filter oauth2Filter() {
        AuthFilter filter = new AuthFilter();
        filter.setSkipFilter(contextPath ->
                contextPath.endsWith("favicon.ico") |
                contextPath.endsWith(".js") |
                contextPath.endsWith(".css") |
                contextPath.startsWith("/public")
        );
        return filter;
    }

}
