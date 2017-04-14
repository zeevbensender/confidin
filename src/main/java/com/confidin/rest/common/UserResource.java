package com.confidin.rest.common;

import com.confidin.model.UserProfile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bensende on 14/04/2017.
 */
@RestController
@RequestMapping("/public/")
public class UserResource {
    @RequestMapping("user/{name}")
    public UserProfile user(@PathVariable String name) {
        return new UserProfile(name, name + "@confidin.com");
    }

    @RequestMapping("user")
    public UserProfile user() {
        String name = Thread.currentThread().getName();
        return new UserProfile(name, name + "@confidin.com");
    }

}
