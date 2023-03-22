package com.grekoff.market.auth.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
class SecurityConfiguration {

    private final KeycloakLogoutHandler keycloakLogoutHandler;

//    SecurityConfiguration(KeycloakLogoutHandler keycloakLogoutHandler) {
//        this.keycloakLogoutHandler = keycloakLogoutHandler;
//    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/welcome/*")
                .permitAll()
                .requestMatchers("/keycloak")
                .authenticated();
        http.oauth2Login()
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler)
                .logoutSuccessUrl("/");
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .build();
//    }
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        return new ClientRegistrationRepository() {
            @Override
            public ClientRegistration findByRegistrationId(String registrationId) {
                return null;
            }
        };
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) throws JwtException {
                return null;
            }
        };
    }


}
