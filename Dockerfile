FROM eclipse-temurin:17-jdk-focal

MAINTAINER lfneves

WORKDIR /app

COPY docker-compose.yml /docker-compose.yml
EXPOSE 8099
COPY . .

CMD ["./gradlew", "bootRun", "--parallel", "--build-cache"]