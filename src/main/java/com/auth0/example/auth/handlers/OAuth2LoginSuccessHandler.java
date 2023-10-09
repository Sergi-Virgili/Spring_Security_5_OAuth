package com.auth0.example.auth.handlers;

import com.auth0.example.users.User;
import com.auth0.example.users.UserRequest;
import com.auth0.example.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
//        System.out.println(attributes);

        User user = createUserEntity(attributes);

        var authUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(user.getRole().name())), attributes, "email");
        Authentication securityAuth = new OAuth2AuthenticationToken(authUser, List.of(new SimpleGrantedAuthority(user.getRole().name())),
                authToken.getAuthorizedClientRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
//        set autentication principa

//        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("/api/private");
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private User createUserEntity(Map<String, Object> attributes) {
        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();

        UserRequest userRequest = UserRequest.builder()
                .email(email)
                .name(name)
                .build();

        return userService.createUser(userRequest);
    }

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws java.io.IOException, ServletException {
//        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
//        auth.getAuthorizedClientRegistrationId(); //Github
//        DefaultOAuth2User principal = (DefaultOAuth2User) auth.getPrincipal();
//        Map<String, Object> attributes = principal.getAttributes();
//        String email;
//        email = Optional.ofNullable(attributes.get("email")).orElse("").toString(); //raro
//        System.out.println(email);
//        System.out.println(principal.getAttributes().get("login"));
//        principal.getAttributes().get("avatar_url");
////        var userRequest = UserRequest.builder()
////                .email(auth)
////                .username(auth.getName())
////                .build();
//        System.out.println(authentication);
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
}
