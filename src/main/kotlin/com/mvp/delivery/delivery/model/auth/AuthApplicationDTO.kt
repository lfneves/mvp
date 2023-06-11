package com.mvp.delivery.delivery.model.auth

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class AuthApplicationDTO(
    @Schema(description = "O ClientID da aplicação") var clientId: String,
    @Schema(description = "O SecretID do ClientID") var secretId: String,
    @Schema(description = "O identificador do Cliente") override var idClient: Long?
): IAuthDTO {

    @Schema(required = false, hidden = true)
    override val username: String = clientId
    @Schema(required = false, hidden = true)
    override val password: String = secretId

    override fun toAuthentication(authorities: List<SimpleGrantedAuthority>): Authentication {
        return AuthenticationVO(
            this, authorities
        )
    }

    @Schema(required = false, hidden = true)
    override fun getClaims(): Map<String, String?> = mapOf("clientId" to clientId, "idClient" to idClient?.toString())
}