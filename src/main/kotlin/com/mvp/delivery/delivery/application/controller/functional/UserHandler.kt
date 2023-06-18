package com.mvp.delivery.delivery.application.controller.functional

import com.mvp.delivery.delivery.infrastruture.entity.user.UserEntity
import com.mvp.delivery.delivery.domain.client.service.user.IUserService
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
                userService.getUsers(), UserEntity::class.java
            )
    }
}