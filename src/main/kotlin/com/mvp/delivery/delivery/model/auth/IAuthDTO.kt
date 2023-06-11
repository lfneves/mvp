package com.mvp.delivery.delivery.model.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

interface IAuthDTO {
    val username: String
    val password: String
    val idClient: Long?

    fun getClaims(): Map<String, String?>

    fun toAuthentication(authorities: List<SimpleGrantedAuthority> = listOf()): Authentication
}