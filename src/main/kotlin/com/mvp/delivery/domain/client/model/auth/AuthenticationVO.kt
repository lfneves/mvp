package com.mvp.delivery.domain.client.model.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class AuthenticationVO (
    val iAuthDTO: IAuthDTO,
    val authorities: List<SimpleGrantedAuthority> = listOf()
): Authentication {

    private var isAuthenticated = authorities.isNotEmpty()

    override fun getName(): String? {
        return iAuthDTO.username
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getCredentials(): String {
        return iAuthDTO.password

    }

    override fun getDetails(): IAuthDTO {
        return iAuthDTO
    }

    override fun getPrincipal(): String? {
        return iAuthDTO.username
    }

    override fun isAuthenticated(): Boolean {
        return this.isAuthenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }
}