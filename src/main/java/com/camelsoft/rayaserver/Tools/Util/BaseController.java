package com.camelsoft.rayaserver.Tools.Util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseController {
    public UserDetails getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            throw new ClassCastException("Authentication principal is not an instance of UserDetails");
        }

        return (UserDetails) authentication.getPrincipal();
    }

}
