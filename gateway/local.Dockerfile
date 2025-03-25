# Deploy and run from locally built jar
FROM amazoncorretto:21-alpine3.21
WORKDIR /app
RUN mkdir target

LABEL maintainer="Rishi Raj"
LABEL version="1.0"
LABEL description="Dockerfile for Charging Session Management System::Gateway with JDK 21, Kafka and MongoDB"

COPY target/gateway-0.0.1-SNAPSHOT.jar /app/target/
EXPOSE 8080
CMD [ "java", "-jar","target/gateway-0.0.1-SNAPSHOT.jar" ]

VOLUME ./data:/data/db
