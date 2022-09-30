
FROM maven:3.8.6 as innrate-agent
ARG PROJECT_NAME=innrate-backend

COPY . /opt/innrate
WORKDIR /opt/innrate

## Build JAR
RUN export TZ=Europe\Moscow && \
    export LANG=ru_RU.utf8 && \
#    export LANG=en_US.utf8 && \
    mvn -ntp verify

RUN PROJECT_VERSION="$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)" && \
    mkdir innrate-backend/target/docker && \
    cp innrate-backend/target/$PROJECT_NAME-$PROJECT_VERSION.jar innrate-backend/target/docker/


FROM bellsoft/liberica-openjdk-debian:18.0.2.1-1 as innrate-service
ARG PROJECT_NAME=innrate-backend

WORKDIR /opt/innrate
COPY --from=innrate-agent /opt/innrate/innrate-backend/target/docker/$PROJECT_NAME-*.jar .

ENV JAVA_OPTS="-Duser.timezone=UTC+03 -Duser.language=ru -Duser.region=RU -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
ENV PROJECT_NAME=${PROJECT_NAME}

ENTRYPOINT ["sh", "-c", "export LANG=en_US.utf8 && JAR_NAME=$(basename -a ${PROJECT_NAME}-*.jar | head -n1) && java ${JAVA_OPTS} -jar ${JAR_NAME}"]

EXPOSE 8090
EXPOSE 5005