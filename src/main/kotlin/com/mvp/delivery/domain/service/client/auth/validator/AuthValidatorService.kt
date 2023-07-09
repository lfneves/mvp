package com.mvp.delivery.domain.service.client.auth.validator

import com.mvp.delivery.domain.model.auth.AuthApplicationDTO
import com.mvp.delivery.domain.model.auth.AuthClientDTO
import com.mvp.delivery.domain.model.auth.AuthenticationVO
import com.mvp.delivery.domain.model.user.UserDTO
import com.mvp.delivery.domain.exception.Exceptions
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthValidatorService {
    fun validate(authentication: Authentication, userDTO: UserDTO): Mono<Authentication> {
        if(userDTO.cpf == null) return Mono.just(authentication)
        return Mono.just(authentication).handle { handleAuthentication, sink ->
            handleAuthentication as AuthenticationVO
            if(handleAuthentication.iAuthDTO.username == userDTO.cpf){
                sink.next(handleAuthentication)
            } else {
                sink.error(Exceptions.NotFoundException("Dado não pertence ao usuário logado."))
            }
        }
    }

    suspend fun validadeByUsername(username: String) {
        val securityContext = ReactiveSecurityContextHolder.getContext().awaitSingle()
        if(securityContext == null) Exceptions.BadCredentialsException("Erro Credenciais")
        val authDTO = (securityContext.authentication as AuthenticationVO).iAuthDTO
        if((authDTO is AuthClientDTO || authDTO is AuthApplicationDTO) && authDTO.username != username) {
            Exceptions.BadCredentialsException("Não tem permissão de acesso.")
        }
    }
}