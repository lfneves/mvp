package com.mvp.delivery.delivery.controller.functional

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class RoutingHandler {
    @Bean
    fun functionalRoutes(userHandler: UserHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(RequestPredicates.GET(apiPrefix)) { request: ServerRequest? -> userHandler.getUsers(request) }
    }
    companion object {
        private const val apiPrefix = "/api/v1/"
    }
}