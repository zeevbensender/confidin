package com.confidin.rest.restricted;

import com.confidin.api.ApiService;
import com.confidin.model.UserProfile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bensende on 14/04/2017.
 */
@RestController
public class Layout {
    //    todo: spring initialization
    ApiService apiService = new ApiService();
    @RequestMapping("/login")
    public String user() {
        return apiService.getProfile();
    }

}
