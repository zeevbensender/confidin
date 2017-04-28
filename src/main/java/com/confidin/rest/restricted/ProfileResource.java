package com.confidin.rest.restricted;

import com.confidin.api.ApiService;
import com.confidin.model.UserProfile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by bensende on 14/04/2017.
 */
@RestController
@RequestMapping("/")
public class ProfileResource {
    //    todo: spring initialization
    private ApiService apiService = new ApiService();
    @RequestMapping("user/{name}")
    public UserProfile user(@PathVariable String name) {
        return new UserProfile(name, name + "@confidin.com");
    }

    @RequestMapping("user")
    public String user() {
        UserProfile user = apiService.getProfile();
//        data.userAuthentication.details.name
        String name = "{\"userAuthentication\":{\"details\":{\"name\":\"voldemar\"}}}";
//        String name = "{ \"name\" : \"" + user.getFirstName() + " " + user.getLastName() + "\"}";
        return name;
//        return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
