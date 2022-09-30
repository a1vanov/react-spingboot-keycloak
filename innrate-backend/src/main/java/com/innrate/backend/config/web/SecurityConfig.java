package com.innrate.backend.config.web;

import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

//@Configuration
//public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
public class SecurityConfig extends WebSecurityConfigurerAdapter {


//    @Override
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new ChangeSessionIdAuthenticationStrategy();
//    }

//    @Override
//    protected void configure(HttpSecurity http) {
    /*
    This is workaround for Spring bug.
    If to comment this method, Spring will use build-in login page instead of KeyCloak login page.
    */
//    }
}
