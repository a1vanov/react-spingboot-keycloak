package com.innrate.common.spring.tomcat;

import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.UpgradeProtocol;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.innrate.common.spring.tomcat.conn.ConnectorProperties;

import javax.servlet.Servlet;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Overrides {@code org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryConfiguration}
 * to substitute {@link TomcatFactory} instead of {@code org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory}.
 *
 * Fixes some issues in {@code TomcatServletWebServerFactory#getWebServer()} method.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, Tomcat.class, UpgradeProtocol.class})
@EnableConfigurationProperties(ConnectorProperties.class)
public class TomcatFactoryConfig {

    @Bean
    TomcatServletWebServerFactory tomcatFactory(
            ConnectorProperties connectorProperties,
            ObjectProvider<TomcatConnectorCustomizer> connectorCustomizers,
            ObjectProvider<TomcatContextCustomizer> contextCustomizers,
            ObjectProvider<TomcatProtocolHandlerCustomizer<?>> protocolHandlerCustomizers) {

        TomcatFactory factory = new TomcatFactory(connectorProperties);

        factory.getTomcatConnectorCustomizers().addAll(collect(connectorCustomizers));
        factory.getTomcatContextCustomizers().addAll(collect(contextCustomizers));
        factory.getTomcatProtocolHandlerCustomizers().addAll(collect(protocolHandlerCustomizers));
        return factory;
    }

    private static <C> Collection<C> collect(ObjectProvider<C> provider) {
        return provider.orderedStream().collect(Collectors.toList());
    }
}
