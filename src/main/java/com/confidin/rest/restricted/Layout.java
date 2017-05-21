package com.confidin.rest.restricted;

import com.confidin.api.ApiService;
import com.confidin.auth.Roles;
import com.confidin.model.UserProfile;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(Roles.ROLE_PROFILE_OWNER)
    public UserProfile user() {
        return apiService.getProfile();
    }

}
