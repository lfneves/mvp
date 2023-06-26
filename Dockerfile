FROM eclipse-temurin:17-jdk-focal

MAINTAINER lfneves
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY docker-compose.yml /docker-compose.yml

EXPOSE 8099
ENTRYPOINT ["java","-jar","app.jar"]

