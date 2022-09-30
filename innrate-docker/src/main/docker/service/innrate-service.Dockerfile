# build manualy
# docker image build -t innrate/innrate:latest .

# Build manualy
#docker image build -t innrate/innrate:0.8.0-SNAPSHOT --build-arg PROJECT_ARTEFACT_ID=innrate --build-arg PROJECT_VERSION=0.1.0-SNAPSHOT .

# Run  manualy
#docker container run -d -p 8090:8090 -v %cd%\src\main\resources\:/opt/innrate -e "JAVA_OPTS=-Dspring.profiles.active=docker -Dspring.config.location=file:/opt/innrate" innrate/innrate:0.1.0-SNAPSHOT
FROM docker.io/amazoncorretto:11.0.15-alpine

ARG PROJECT_ARTEFACT_ID
ARG PROJECT_VERSION
ARG JAR_NAME=${PROJECT_ARTEFACT_ID}-${PROJECT_VERSION}

RUN echo "JAR_NAME=${JAR_NAME}"

WORKDIR /opt/innrate/

COPY target/${JAR_NAME}.jar ${JAR_NAME}.jar

ENV JAVA_OPTS=""
ENV JAR_NAME=${JAR_NAME}

CMD java $JAVA_OPTS -jar ${JAR_NAME}.jar
