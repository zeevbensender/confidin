package com.confidin.rest.restricted;

import com.confidin.api.ApiService;
import com.confidin.auth.LinkedinPrincipal;
import com.confidin.model.UserProfile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bensende on 14/04/2017.
 */
@RestController
@RequestMapping("/")
public class ProfileResource {
    @RequestMapping("user/{name}")
    public UserProfile user(@PathVariable String name) {
        return new UserProfile(name, name + "@confidin.com");
    }

    @RequestMapping("user")
    public Map<String,Map<String, Map<String, String>>> user() {
        String fullName = ((LinkedinPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName();
        Map<String, String> details = new HashMap<>();
        details.put("name", fullName);
        Map<String, Map<String, String>> uAuth = new HashMap<>();
        uAuth.put("details", details);
        Map<String,Map<String, Map<String, String>>> result = new HashMap<>();
        result.put("userAuthentication", uAuth);
        return result;
//        String result = "{\"userAuthentication\":{\"details\":{\"name\":\"" + fullName + "\"}}}";
//        return result;
    }

}
