package com.mvp.delivery.delivery.controller

import com.mvp.delivery.delivery.model.auth.AuthClientDTO
import com.mvp.delivery.delivery.model.auth.AuthTokenDTO
import com.mvp.delivery.delivery.utils.JWTUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class AuthController() {

    @Autowired
    private lateinit var authenticationManager: ReactiveAuthenticationManager

    @PostMapping("/login-token")
    @Operation(summary = "Authenticação usuário + senha", description = "Se authenticado corretamente, retorno o token de Authorização nas headers")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Successful login token"),
            ApiResponse(responseCode = "404", description = "Error user does not exist"),
        ]
    )
    fun token(@RequestBody authDTO: AuthClientDTO): Mono<AuthTokenDTO> {
        return Mono.just(authDTO).flatMap {
            authenticationManager.authenticate(it.toAuthentication())
        }.map {
            val token = JWTUtils.createToken(authDTO)
            AuthTokenDTO(token)
        }
    }
}