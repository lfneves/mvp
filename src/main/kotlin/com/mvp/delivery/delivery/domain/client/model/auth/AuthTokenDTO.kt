package com.mvp.delivery.delivery.domain.client.model.auth

import io.swagger.v3.oas.annotations.media.Schema

class AuthTokenDTO(
    @Schema(description = "O token a ser utilizado para acessar os serviços que precisam de autorização.") var token: String
)