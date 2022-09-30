package com.innrate.common.config.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


/**
 * Spring-security config for suitable for React and Angular.
 */
public class ReactSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        Sets TokenRepository instead of HttpSessionCsrfTokenRepository set by default.
        TokenRepository persists the CSRF token in a cookie named "XSRF-TOKEN" and reads
        from the header "X-XSRF-TOKEN" following the conventions of AngularJS and React

        withHttpOnlyFalse() means that cookie XSRF-TOKEN is set without HttpOnly flag and can be read form JavaScript.

        HttpSessionCsrfTokenRepository persists the CSRF token in session. In this case application code is responsible
        for adding token into a response.
        */
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
