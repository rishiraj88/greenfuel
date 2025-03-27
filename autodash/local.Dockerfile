# Deploy and run from locally built jar
FROM amazoncorretto:21-alpine3.21
WORKDIR /app
RUN mkdir target

LABEL maintainer="Rishi Raj"
LABEL version="1.0"
LABEL description="Dockerfile for Charging Session Management System::Auto Dashboard (Driver app) with JDK 21"

COPY target/autodash-0.0.1-SNAPSHOT.jar /app/target/
EXPOSE 8084
CMD [ "java", "-jar","target/autodash-0.0.1-SNAPSHOT.jar" ]
