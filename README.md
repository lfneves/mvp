# MVP - Tech challenge

#### This is an [Spring Boot WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) app using [Kotlin](https://kotlinlang.org/).

Spring WebFlux uses [Reactor](https://projectreactor.io/) library, an implementation of Reactive Streams
Specs for building non-blocking applications.

This project:
* Uses [Reactor Netty](https://github.com/reactor/reactor-netty), the default Web container for Spring WebFlux
* Uses functional endpoints
* Uses [PostgreSQL](https://www.postgresql.org/) database


## 💡 Requirements

#### Java 17 or later - [SDKMAN - Recommendation](https://sdkman.io/install)

#### Gradle 7.6.1 or later - [Gradle build tool Installation](https://gradle.org/install/)

#### Docker 24.0.2 or later - [How install Docker](https://docs.docker.com/engine/install/)

#### Docker Compose 1.29.2 or later - [Reference guide](https://docs.docker.com/compose/install/)

#### Project works on port 8099 (http://localhost:8099)

<!-- GETTING STARTED -->
## Getting Started

```sh
# Get the latest version

git clone https://github.com/lfneves/mvp.git
```


### Prerequisites
Check versions
* Java 17+
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

Main project directory
* Gradle
  ```sh
  ./gradlew clean
  ./gradlew bootJar
  
  or 
  
  ./gradlew clean bootJar
  ```
  
  Docker build and start applications
    ```sh
  docker-compose up --build
   or
  docker-compose up -d --build
  ```

  To recreate the application, in case of problems use the command
    ```sh
  docker-compose down
  ```

---
### This project uses [CommandLineRunner](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/CommandLineRunner.html)
- CommandLineRunner is used to create a default user, products and categories on start application.
- Default login :

**/api/auth/login-token**
```json
{
  "username": "99999999999",
  "password": "admin"
}
```
---

### The best way to use it as a suggestion is using [Postman](https://www.postman.com/downloads/)
#### A collection is available preconfigured in the project root
[MVP - Pos tech delivery application.postman_collection.json]()

- This project uses user and session control for access
- Endpoints without control access  _**"/api/auth/*"**_, **_"/api/v1/users/signup"_**

#### Create new user example:
http://localhost:8099/api/v1/users/signup

Body:
```json
{
    "name": "Admin",
    "email": "admin@email.com",
    "cpf": "99999999999",
    "password": "123",
    "address": {
        "street": "rua 1",
        "city": "sp",
        "state": "sp",
        "postalCode": "1234"
    }
}
```

<br/>

#### Login - Use the username(cpf) and password copy the token and use it in authenticated endpoints.

http://localhost:8099/api/auth/login-token
```json
{
  "username": "99999999999",
  "password": "123"
}
```
Response:
```json
{
"token": "eyJhbGciOiJIUzI1NiJ9.eyJpZENsaWVudCI6IjAiLCJ1c2VybmFtZSI6IjEyMzQ1Njc4OTEyIiwic3ViIjoiMTIzNDU2Nzg5MTIiLCJpYXQiOjE2ODgwOTI1NTAsImF1ZCI6Im5vLWFwcGxpY2F0aW9uLW5hbWUiLCJleHAiOjE2ODgwOTQwMDB9.HagYPqukwOML3OYad8sRjlnE0Gsy-5tGUSC72S-xyfU"
}
```

### 💡To make it easier use environment variables 
#### Place the command in the test tab on /api/auth/login-token
```sh
pm.environment.set("token", pm.response.json().token);
```
![Alt text](images/postman_01.png "Postman token environment")
![Alt text](images/postman_02.png "Postman token using")
---

### This project also uses OpenAPI Specification [(Swagger)](https://swagger.io/docs/specification/about/).

#### To access swagger use the URL:
http://localhost:8099/swagger-ui.html
or
http://localhost:8099/webjars/swagger-ui/index.html

![Alt text](images/swagger_01.png "Swagger home")

![Alt text](images/swagger_02.png "Swagger Endpoints")

---

<!-- ROADMAP -->
## Roadmap

- [x] Improve README.md
- [X] Update order add paid status and adjusting service
- [x] Create checkout endpoint(simple version)
- [x] Add time wait Order after payment
- ### Improvements
- [x] Refactor admin services and repository to new package
- [ ] Reduce endpoints path size
- [ ] Create validator example: username and email
- [ ] Fix create order exceptions

---
<!-- LICENSE -->
## License

Distributed under the MIT License. See LICENSE.txt for more information.

