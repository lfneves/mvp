package com.mvp.delivery.application.controller.functional

//import com.mvp.delivery.domain.client.service.user.UserService
//import com.mvp.delivery.infrastruture.entity.user.UserEntity
//import org.springframework.http.MediaType
//import org.springframework.stereotype.Component
//import org.springframework.web.reactive.function.server.ServerRequest
//import org.springframework.web.reactive.function.server.ServerResponse
//import reactor.core.publisher.Mono
//
//@Component
//class UserHandler(userService: UserService) {
//    private val userService: UserService
//
//    init {
//        this.userService = userService
//    }
//
//    fun getUsers(request: ServerRequest?): Mono<ServerResponse> {
//        return ServerResponse.ok()
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(
//                userService.getUsers(), UserEntity::class.java
//            )
//    }
//}