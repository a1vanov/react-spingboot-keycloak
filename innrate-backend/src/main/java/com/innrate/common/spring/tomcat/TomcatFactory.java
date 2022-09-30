package com.innrate.common.spring.tomcat;

import org.apache.catalina.Engine;
import org.apache.catalina.Service;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.modeler.Registry;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.SslStoreProvider;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import com.innrate.common.ReflectUtil;
import com.innrate.common.spring.tomcat.conn.ConnectorBuilder;
import com.innrate.common.spring.tomcat.conn.ConnectorProperties;

import java.io.File;
import java.util.Collection;

public class TomcatFactory extends TomcatServletWebServerFactory {

    private final ConnectorProperties connectorProperties;

    public TomcatFactory(ConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    @Override
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        if (getDisableMBeanRegistry()) {
            Registry.disableRegistry();
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(readBaseDir());
        tomcat.getHost().setAutoDeploy(false);

        configureConnectors(tomcat);
        configureEngineOverride(tomcat.getEngine());

        prepareContext(tomcat.getHost(), initializers);
        return getTomcatWebServer(tomcat);
    }

    private void configureConnectors(Tomcat tomcat) {
        Service service = tomcat.getService();

        for (ConnectorProperties.Connector connProps : connectorProperties.getConnectors()) {
            if (connProps.isEnabled()) {
                Connector c = buildConnector(connProps);
                service.addConnector(c);
            }
        }

        for (Connector connector : getAdditionalTomcatConnectors()) {
            service.addConnector(connector);
        }
    }

    /**
     * Builds a connector.
     * "Unchecked" suppressing is used due to wrong generic usage in #getTomcatProtocolHandlerCustomizers() method.
     *
     * @param props connector properties
     * @return built connector
     */
    @SuppressWarnings("unchecked")
    private Connector buildConnector(ConnectorProperties.Connector props) {

        ConnectorBuilder builder = ConnectorBuilder.create(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
                .setAddress(getAddress())
                .setUriEncoding(getUriEncoding())
                .setPort(props.getPort())
                .setServerHeader(getServerHeader())
                .applyToProtocol((Collection) getTomcatProtocolHandlerCustomizers());

        Ssl ssl = props.getSsl();
        if (ssl != null && ssl.isEnabled()) {
            builder.apply(createSslCustomizer(ssl, getSslStoreProvider()));
        }

        return builder
                .apply(getTomcatConnectorCustomizers())
                .apply(createCompressionCustomizer(getCompression()))
                .build();
    }

    private TomcatConnectorCustomizer createSslCustomizer(Ssl ssl, SslStoreProvider sslStoreProvider) {
        String customizerClassName = "org.springframework.boot.web.embedded.tomcat.SslConnectorCustomizer";

        return ReflectUtil.newInstance(
                ReflectUtil.getConstructor(customizerClassName, Ssl.class, SslStoreProvider.class),
                ssl,
                sslStoreProvider);
    }

    private TomcatConnectorCustomizer createCompressionCustomizer(Compression compression) {
        String customizerClassName = "org.springframework.boot.web.embedded.tomcat.CompressionConnectorCustomizer";

        return ReflectUtil.newInstance(
                ReflectUtil.getConstructor(customizerClassName, Compression.class),
                compression);
    }

    private String readBaseDir() {
        File baseDir = getBaseDir();

        if (baseDir == null) {
            baseDir = createTempDir("tomcat");
        }

        return baseDir.getAbsolutePath();
    }

    private void configureEngineOverride(Engine engine) {
        engine.setBackgroundProcessorDelay(getBackgroundProcessorDelay());

        for (Valve valve : getEngineValves()) {
            engine.getPipeline().addValve(valve);
        }
    }

    public boolean getDisableMBeanRegistry() {
        return ReflectUtil.readField(this, "disableMBeanRegistry");
    }

    public int getBackgroundProcessorDelay() {
        return ReflectUtil.readField(this, "backgroundProcessorDelay");
    }

    public File getBaseDir() {
        return ReflectUtil.readField(this, "baseDirectory");
    }
}
