package com.mvp.delivery.application.security

import com.mvp.delivery.domain.client.model.auth.AuthClientDTO
import com.mvp.delivery.domain.client.model.auth.IAuthDTO
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException

object AuthClientDeserializer {

    fun getInstance(claims: Claims): IAuthDTO {
        if(claims.containsKey("idClient")){
            return AuthClientDTO(claims["idClient"].toString().toLong(), "", "")
        }
        throw JwtException("Não conseguimos deserializar o JWT")
    }
}
