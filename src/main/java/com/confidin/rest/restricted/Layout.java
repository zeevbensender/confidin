package com.confidin.rest.restricted;

import com.confidin.model.UserProfile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bensende on 14/04/2017.
 */
@RestController
public class Layout {
    @RequestMapping("/login")
    public UserProfile user() {
        return new UserProfile("ostap", "loggedin@confidin.com");
    }

}
