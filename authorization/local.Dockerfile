# Deploy and run from locally built jar
FROM amazoncorretto:21-alpine3.21
WORKDIR /app
RUN mkdir target

LABEL maintainer="Rishi Raj"
LABEL version="1.0"
LABEL description="Dockerfile for Charging Session Management System::Authorization Service with JDK 21, Kafka and MongoDB"

COPY target/authorization-0.0.1-SNAPSHOT.jar /app/target/
EXPOSE 8082
CMD [ "java", "-jar","target/authorization-0.0.1-SNAPSHOT.jar" ]

VOLUME ./data:/data/db
