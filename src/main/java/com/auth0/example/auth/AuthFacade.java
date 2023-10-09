package com.auth0.example.auth;

import com.auth0.example.users.User;
import com.auth0.example.users.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade {

    private final UserService userService;

    public AuthFacade(UserService userService) {
        this.userService = userService;
    }

    public User getAuthUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByMail(email).orElseThrow(() -> new RuntimeException("Need to Login"));
    }
}
