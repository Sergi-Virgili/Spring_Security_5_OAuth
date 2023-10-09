package com.auth0.example.auth.config;

import com.auth0.example.auth.handlers.LogoutHandler;
import com.auth0.example.auth.handlers.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final LogoutHandler logoutHandler;
    private final com.auth0.example.auth.handlers.OAuth2LoginSuccessHandler OAuth2LoginSuccessHandler;

    public SecurityConfig(LogoutHandler logoutHandler,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.logoutHandler = logoutHandler;
        OAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // disable CSRF protection for requests to the H2 database console
                .csrf(csrf -> csrf
                .ignoringRequestMatchers(toH2Console())
                .disable()
        )
                // allow requests endpoints database console
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(toH2Console()).permitAll();
                    auth.requestMatchers(new AntPathRequestMatcher("/api/public")).permitAll();
                    auth.requestMatchers(new AntPathRequestMatcher("/api/private")).authenticated();
                    auth.mvcMatchers("/", "/images/**").permitAll();

                })
                .oauth2Login(auth -> auth.successHandler(OAuth2LoginSuccessHandler))

                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .logout()

                .addLogoutHandler(logoutHandler);

        return http.build();

    }


// If using HS256, create a Bean to specify the HS256 should be used. By default, RS256 will be used.
//    @Bean
//    public JwtDecoderFactory<ClientRegistration> idTokenDecoderFactory() {
//        OidcIdTokenDecoderFactory idTokenDecoderFactory = new OidcIdTokenDecoderFactory();
//        idTokenDecoderFactory.setJwsAlgorithmResolver(clientRegistration -> MacAlgorithm.HS256);
//        return idTokenDecoderFactory;
//    }

}
