package com.mvp.delivery.delivery.controller.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CustomReactiveAuthenticationConverter @Autowired constructor(
    private val jwtAuthenticationConverter: JWTAuthenticationConverter
): ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        return jwtAuthenticationConverter.convert(exchange)
    }
}