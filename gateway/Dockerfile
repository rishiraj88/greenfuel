# Build
# https://github.com/carlossg/docker-maven/blob/8cfe24baffa5b250f7bb2d31ce233fc28f3c4f20/amazoncorretto-21/Dockerfile
FROM maven:3.9.9-amazoncorretto-21-alpine AS build
WORKDIR /app
COPY ./src src
COPY ./pom.xml .
RUN mvn package

# Deploy
FROM amazoncorretto:21-alpine3.21 AS deploy
WORKDIR /app
RUN mkdir target

LABEL maintainer="Rishi Raj"
LABEL version="1.0"
LABEL description="Dockerfile for Charging Session Management System::Gateway with JDK 21, Confluent Kafka and MongoDB"

COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar /app/target/
EXPOSE 8080
CMD [ "java", "-jar","target/gateway-0.0.1-SNAPSHOT.jar" ]

VOLUME ./data:/data/db
