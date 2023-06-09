package com.mvp.delivery.delivery.controller.functional

import com.mvp.delivery.delivery.model.User
import com.mvp.delivery.delivery.service.UserService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class UserHandler(userService: UserService) {
    private val userService: UserService

    init {
        this.userService = userService
    }

    fun getUsers(request: ServerRequest?): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                userService.getUsers(), User::class.java
            )
    }
}