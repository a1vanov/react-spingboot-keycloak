package com.innrate.backend.service;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import com.innrate.backend.service.common.InnrateController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Be careful, we use keycloak-spring-boot-adapter, NOT keycloak-spring-security-adapter
 * So spring security is not used at all.
 * Main keycloak magic happens in {@code CatalinaSessionTokenStore}, {@code KeycloakAuthenticatorValve}
 * and {@code OAuthRequestAuthenticator} classes.
 */
@InnrateController
public class LogoutController {

    private final String logoutPath;

    @Autowired
    public LogoutController(@Value("${innrate.frontend-path-prefix}") String pathPrefix) {
        this.logoutPath = pathPrefix + "/logout.html";
    }

    /**
     * Makes SSO Logout and redirect to logout page.
     * This endpoint has to be private. Otherwise there will be no token to send logout to KeyCloak.
     *
     * @param request the request
     * @throws ServletException if tomcat session logout throws exception
     */
    @PostMapping(path = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        keycloakSessionLogout(request);
        tomcatSessionLogout(request);
        response.sendRedirect(logoutPath);
    }

    private void keycloakSessionLogout(HttpServletRequest request) {
        RefreshableKeycloakSecurityContext c = getKeycloakSecurityContext(request);
        KeycloakDeployment d = c.getDeployment();
        c.logout(d);
    }

    private void tomcatSessionLogout(HttpServletRequest request) throws ServletException {
        request.logout();
    }

    private RefreshableKeycloakSecurityContext getKeycloakSecurityContext(HttpServletRequest request) {
        return (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }
}
