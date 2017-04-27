package com.confidin.api;

import com.confidin.auth.AccessToken;
import com.confidin.model.UserProfile;
import org.codehaus.jackson.map.ObjectMapper;
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
    ObjectMapper mapper = new ObjectMapper();
    private String path = "https://api.linkedin.com/v1/people/~?format=json";

    public UserProfile getProfile(){
        try {
            ApiCallResult result = client.sendGet(path, "Get Profile");
            UserProfile userProfile = mapper.readValue(result.getResponse() , UserProfile.class);

            return userProfile;
        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
