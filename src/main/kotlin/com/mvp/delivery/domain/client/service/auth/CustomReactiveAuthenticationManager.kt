package com.mvp.delivery.domain.client.service.auth

import com.mvp.delivery.domain.client.model.auth.AuthClientDTO
import com.mvp.delivery.domain.client.model.auth.AuthenticationVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomReactiveAuthenticationManager @Autowired constructor(
    private val authClientService: AuthClientService,
): ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        if(authentication !is AuthenticationVO) throw BadCredentialsException("Usuário ou senha errados.")
        if(authentication.isAuthenticated) return Mono.just(authentication)
        if(authentication.iAuthDTO is AuthClientDTO) return authClientService.authenticate(authentication)
        return Mono.just(authentication)
    }
}