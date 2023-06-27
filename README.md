# MVP - Tech challenge

This is an [Spring Boot WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
app using [Koltin](https://kotlinlang.org/). Spring WebFlux uses [Reactor](https://projectreactor.io/) library, an implementation of Reactive Streams
Specs for building non-blocking applications.

This project:
* Uses [Reactor Netty](https://github.com/reactor/reactor-netty), the default Web container for Spring WebFlux
* Uses functional endpoints
* Uses [PostgreSQL](https://www.postgresql.org/) database


## 💡 Requirements

Java 17 or later - [SDKMAN - Recommendation](https://sdkman.io/install)

Gradle - [Gradle build tool Installation](https://gradle.org/install/)

Docker - [How install Docker](https://docs.docker.com/engine/install/)

Docker Compose - [Reference guide](https://docs.docker.com/compose/install/)

<!-- GETTING STARTED -->
## Getting Started
## 📲 Installation

### Prerequisites
Check versions
* Java
  ```sh
  java --version
  ```

* Docker
  ```sh
  docker -v
  ```

* Docker Compose
  ```sh
  docker-compose --version
  ```

### Installation
This is an example of how to use the software and how to install them.
* gradle
  ```sh
  ./gradle clean
  ```
  Generate jar file
    ```sh
  ./gradle bootJar
  ```
  Docker build and start applications
    ```sh
  docker-compose up --build
  ```


<!-- ROADMAP -->
## Roadmap

- [ ] Update order add paid status and adjusting service
- [ ] Create checkout endpoint
- [ ] Refactor admin services and repository to new package
- [ ] Fix create order exceptions

<!-- LICENSE -->
## License

Distributed under the MIT License. See LICENSE.txt for more information.

