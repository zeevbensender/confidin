package com.confidin.auth;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

/**
 * Created by bensende on 23/04/2017.
 */
public class AccessToken {

    @JsonProperty("access_token")
    private String value;
    @JsonProperty("expires_in")
    private long expiresIn;
    private long expirationTime = -1;
    private String token;

    public AccessToken() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        expirationTime = System.currentTimeMillis() + expiresIn * 1000;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "value='" + value + '\'' +
                ", expiresIn=" + expiresIn +
                ", expirationTime=" + new Date(expirationTime) +
                '}';
    }
}
