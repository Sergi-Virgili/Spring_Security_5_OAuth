package com.auth0.example.demo_controllers;

import com.auth0.example.auth.AuthFacade;
import com.auth0.example.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
// For simplicity of this sample, allow all origins. Real applications should configure CORS for their use case.
public class ApiController {

    @Autowired
    private AuthFacade authFacade;

    @GetMapping(value = "/public")
    public Message publicEndpoint() {
        return new Message("All good. You DO NOT need to be authenticated to call /api/public.");
    }


    @GetMapping(value = "/private")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Message privateEndpoint() {

        User authUser = authFacade.getAuthUser();
        System.out.println(authFacade.getAuthUser());
        return new Message("All good. You can see this because you are Authenticated. y eres:"
                + authUser.getName() + " "
                + authUser.getEmail());

    }

    @GetMapping(value = "/private-scoped")
    public Message privateScopedEndpoint() {
        return new Message("All good. You can see this because you are Authenticated with a Token granted the 'read:messages' scope");
    }
}
