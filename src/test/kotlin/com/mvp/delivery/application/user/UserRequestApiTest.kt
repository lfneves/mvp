package com.mvp.delivery.application.user

import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.helpers.UserMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

class UserRequestApiTest(
    @Autowired val client: WebTestClient,
) {
    fun signup(request: UserDTO = UserMock.mockUserRegistrationRequest()): UserDTO = client.post()
        .uri("/api/v1/users/signup")
        .bodyValue(request)
        .exchange()
        .expectBody<UserDTO>()
        .returnResult()
        .responseBody!!

    fun getAll(): UserDTO = client.get()
        .uri("/api/v1/users")
        .exchange()
        .expectBody<UserDTO>()
        .returnResult()
        .responseBody!!

    fun getById(id: Int): UserDTO = client.get()
        .uri("/api/v1/users/$id")
        .exchange()
        .expectBody<UserDTO>()
        .returnResult()
        .responseBody!!

    fun deleteUserById(id: Int): UserDTO = client.delete()
        .uri("/api/v1/users")
        .exchange()
        .expectBody<UserDTO>()
        .returnResult()
        .responseBody!!

    fun updateUser(id: Int, request: UserDTO = UserMock.mockUser()): UserDTO = client.put()
        .uri("/api/v1/users/update-user/$id")
        .bodyValue(request)
        .exchange()
        .expectBody<UserDTO>()
        .returnResult()
        .responseBody!!
}