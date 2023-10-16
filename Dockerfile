#FROM eclipse-temurin:17-jdk-focal
#
#MAINTAINER lfneves
#
#WORKDIR /app
#
#COPY docker-compose.yml /docker-compose.yml
#EXPOSE 8099
#COPY . .
#
#CMD ["./gradlew", "bootRun", "--parallel", "--build-cache"]

#FROM eclipse-temurin:17-jdk-focal
#
#WORKDIR /app
#
#COPY . .
#
#MAINTAINER lfneves
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#COPY docker-compose.yml /docker-compose.yml
#
#EXPOSE 8099
#ENTRYPOINT ["java","-jar","app.jar"]

FROM eclipse-temurin:17-jdk-focal AS builder

# Copy your Gradle project to the image
COPY . /home/gradle/src

# Set the working directory
WORKDIR /home/gradle/src

# Build the Java artifact
RUN gradle build

# Copy the JAR file to the image context
RUN cp build/libs/*.jar /home/gradle/app.jar

# Copy the JAR file from the image context into the final image
COPY --from=builder /home/gradle/app.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]
