package com.mvp.delivery.domain.client.service.auth

import com.mvp.delivery.domain.client.model.auth.AuthenticationVO
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.repository.user.UserRepository
import com.mvp.delivery.utils.Sha512PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class AuthClientService @Autowired constructor(
    private val passwordEncoder: PasswordEncoder = Sha512PasswordEncoder(),
    private val userRepository: UserRepository
) {

    fun authenticate(authenticationVO: AuthenticationVO): Mono<Authentication> {
        return Mono.just(authenticationVO).flatMap { authentication ->
            val username = authentication.principal
            val password = authentication.credentials
            userRepository.findByUsernameWithAddress(username)
                .mapNotNull { client ->
                    if(!passwordEncoder.matches(password, client.password)) throw Exceptions.BadCredentialsException("Usuário ou senha errados.")
                authentication.isAuthenticated = true
                authentication
            }
                .switchIfEmpty(Mono.error(Exceptions.BadCredentialsException("Usuário ou senha errados.")))
                .toMono()
        }
    }
}