#FROM keycloak/keycloak:17.0.1 as builder
FROM keycloak/keycloak:18.0.0 as builder

COPY --chown=keycloak docker/keycloak/conf /opt/keycloak/conf
COPY --chown=keycloak docker/keycloak/data /opt/keycloak/data

ENV KC_DB=mysql
# Install custom providers
RUN curl -sL https://github.com/aerogear/keycloak-metrics-spi/releases/download/2.5.3/keycloak-metrics-spi-2.5.3.jar -o /opt/keycloak/providers/keycloak-metrics-spi-2.5.3.jar
RUN /opt/keycloak/bin/kc.sh build

#FROM keycloak/keycloak:17.0.1
FROM keycloak/keycloak:18.0.0
COPY --from=builder /opt/keycloak/ /opt/keycloak/
WORKDIR /opt/keycloak
# for demonstration purposes only, please make sure to use proper certificates in production instead
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "cn=KeyCloak,o=InnRate,c=RU" -alias server -ext "SAN:c=DNS:localhost" -keystore conf/server.keystore

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]