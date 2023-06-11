package com.mvp.delivery.delivery.controller

import com.mvp.delivery.delivery.model.auth.AuthClientDTO
import com.mvp.delivery.delivery.model.auth.AuthTokenDTO
import com.mvp.delivery.delivery.utils.JWTUtils
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class AuthController() {

    @Autowired
    private lateinit var authenticationManager: ReactiveAuthenticationManager

    @PostMapping("/token")
    @Operation(summary = "Authenticação usuário + senha", description = "Se authenticado corretamente, retorno o token de Authorização nas headers")
    fun token(@RequestBody authDTO: AuthClientDTO): Mono<AuthTokenDTO> {
        return Mono.just(authDTO).flatMap {
            authenticationManager.authenticate(it.toAuthentication())
        }.map {
            val token = JWTUtils.createToken(authDTO)
            AuthTokenDTO(token)
        }
    }

//    @PostMapping("/token-by-client")
//    @Operation(summary = "Authenticação clientID + secretID", description = "Se authenticado corretamente, retorno o token de Authorização nas headers")
//    fun token(@RequestBody authDTO: AuthClientDTO): Mono<AuthTokenDTO> {
//        return Mono.just(authDTO).flatMap {
//            authenticationManager.authenticate(it.toAuthentication())
//        }.map {
//            val token = JWTUtils.createToken(authDTO)
//            AuthTokenDTO(token)
//        }
//    }
}