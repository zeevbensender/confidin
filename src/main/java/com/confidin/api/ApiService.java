package com.confidin.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by bensende on 23/04/2017.
 */
public class ApiService {
    private final static Logger LOG = LoggerFactory.getLogger(ApiService.class);
    //    todo: spring initialization
    private LinkedinClient client = new LinkedinClient();
    private String path = "https://api.linkedin.com/v1/people/~?format=json";

    public String getProfile(){
        try {
            ApiCallResult result = client.sendGet(path, "Get Profile");
            return result.getResponse();
        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
