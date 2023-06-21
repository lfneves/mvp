package com.mvp.delivery.domain.client.service.auth.validator

import com.mvp.delivery.domain.client.model.auth.AuthenticationVO
import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.domain.exception.Exceptions
import org.springframework.security.core.Authentication
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
}