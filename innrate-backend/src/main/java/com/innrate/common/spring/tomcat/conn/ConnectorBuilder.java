package com.innrate.common.spring.tomcat.conn;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * This class supports building a connection according to {@code TomcatServletWebServerFactory#getWebServer()} method.
 * So some extra checks have come from there.
 */
public class ConnectorBuilder {

    private final Connector connector;

    private ConnectorBuilder(Connector connector) {
        this.connector = connector;
    }

    public static ConnectorBuilder create(String protocol) {
        Connector c = new Connector(protocol);
        c.setThrowOnFailure(true);
        // Don't bind to the socket prematurely if ApplicationContext is slow to start
        c.setProperty("bindOnInit", "false");

        return new ConnectorBuilder(c);
    }

    public Connector build() {
        return connector;
    }

    public ConnectorBuilder setAddress(InetAddress address) {
        ProtocolHandler protocol = connector.getProtocolHandler();

        if (address != null && protocol instanceof AbstractProtocol) {
            ((AbstractProtocol<?>) protocol).setAddress(address);
        }

        return this;
    }

    public ConnectorBuilder setUriEncoding(Charset uriEncoding) {
        if (uriEncoding != null) {
            connector.setURIEncoding(uriEncoding.name());
        }
        return this;
    }

    public ConnectorBuilder setPort(int port) {
        connector.setPort(port);
        return this;
    }

    public ConnectorBuilder setServerHeader(String serverHeader) {
        if (StringUtils.hasText(serverHeader)) {
            connector.setAttribute("server", serverHeader);
        }
        return this;
    }

    public ConnectorBuilder applyToProtocol(Collection<TomcatProtocolHandlerCustomizer<ProtocolHandler>> customizers) {
        customizers.forEach(c -> c.customize(connector.getProtocolHandler()));
        return this;
    }

    public ConnectorBuilder apply(Collection<TomcatConnectorCustomizer> customizers) {
        customizers.forEach(c -> c.customize(connector));
        return this;
    }

    public ConnectorBuilder apply(TomcatConnectorCustomizer customizer) {
        customizer.customize(connector);
        return this;
    }
}
