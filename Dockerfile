FROM openjdk:17

VOLUME /tmp
EXPOSE 8099
ARG JAR_FILE=/build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
#WORKDIR /app
#COPY docker-compose.yml /app/docker-compose.yml
ENTRYPOINT ["java","-jar","app.jar"]
