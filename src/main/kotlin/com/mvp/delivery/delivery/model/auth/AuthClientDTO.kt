package com.mvp.delivery.delivery.model.auth

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class AuthClientDTO(
    @Schema(description = "O identificador do Cliente") override var idClient: Long,
    @Schema(description = "Senha do Cliente") override var password: String
): IAuthDTO {

    @Schema(required = false, hidden = true)
    override val username: String = idClient.toString()

    @Schema(required = false, hidden = true)
    override fun toAuthentication(authorities: List<SimpleGrantedAuthority>): Authentication {
        return AuthenticationVO(
            this, authorities
        )
    }

    @Schema(required = false, hidden = true)
    override fun getClaims(): Map<String, String> = mapOf("idClient" to idClient.toString())
}