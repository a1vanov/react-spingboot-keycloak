package com.innrate.common.spring.tomcat.conn;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.server.Ssl;

import java.util.List;

@ConfigurationProperties(prefix = "server")
@Getter
@Setter
public class ConnectorProperties {

    private List<Connector> connectors;

    @Getter
    @Setter
    public static class Connector {

        /**
         * "Enabled" connector status.
         * The field gives us an opportunity to disable unnecessary predefined connectors.
         * It is useful for docker configurations.
         */
        private boolean enabled = true;

        /**
         * Server HTTP port.
         */
        private Integer port;

        @NestedConfigurationProperty
        private Ssl ssl;
    }
}
