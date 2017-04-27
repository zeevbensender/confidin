package com.confidin.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by bensende on 14/04/2017.
 */
public class UserProfile {
    private String firstName;
    private String lastName;
    private String headline;
    private String id;
    private SiteStandardProfileRequest siteStandardProfileRequest;


    public SiteStandardProfileRequest getSiteStandardProfileRequest() {
        return siteStandardProfileRequest;
    }

    public void setSiteStandardProfileRequest(SiteStandardProfileRequest siteStandardProfileRequest) {
        this.siteStandardProfileRequest = siteStandardProfileRequest;
    }

    public UserProfile(String name, String email) {
        this.firstName = name;
        this.lastName = email;
    }

    public UserProfile() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public class SiteStandardProfileRequest{
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
