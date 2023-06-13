package com.mvp.delivery.delivery.controller.functional

import com.mvp.delivery.delivery.model.User
import com.mvp.delivery.delivery.service.user.IUserService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class UserHandler(IUserService: IUserService) {
    private val userService: IUserService

    init {
        this.userService = IUserService
    }

    fun getUsers(request: ServerRequest?): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                userService.getUsers(), User::class.java
            )
    }
}