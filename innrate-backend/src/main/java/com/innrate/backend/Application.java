package com.innrate.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
        info = @Info(title = "InnRate", description = "InnRate service", version = "1.0.0"),
        security = @SecurityRequirement(
                name = "product5-oauth2",
                scopes = {"openid", "email", "roles"})
)
@SecurityScheme(
        name = "product5-oauth2",
        type = SecuritySchemeType.OAUTH2,
        openIdConnectUrl = "${keycloak.auth-server-url}/realms/${keycloak.realm}/.well-known/openid-configuration",
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "${keycloak.auth-server-url}/realms/${keycloak.realm}/protocol/openid-connect/auth",
                        tokenUrl = "${keycloak.auth-server-url}/realms/${keycloak.realm}/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "openid"),
                                @OAuthScope(name = "email", description = "Employee email"),
                                @OAuthScope(name = "roles", description = "Employee roles")
                        }
                )
        )
)
@SpringBootApplication(
        scanBasePackages = {"com.innrate.backend", "com.innrate.common"},
        exclude = {
                // spring-security
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                // has dependency on spring-security
                org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class
        })
@EnableScheduling
@EnableKafka
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}