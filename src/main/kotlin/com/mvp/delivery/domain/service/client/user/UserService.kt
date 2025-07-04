package com.mvp.delivery.domain.service.client.user

import com.mvp.delivery.domain.model.user.UserDTO
import com.mvp.delivery.domain.model.user.UsernameDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono

interface UserService {
    fun getUserById(id: Int, authentication: Authentication): Mono<UserDTO>

    fun getByUsername(usernameDTO: UsernameDTO, authentication: Authentication): Mono<UserDTO>

    fun saveUser(user: UserDTO): Mono<UserDTO>

    fun signup(user: UserDTO): Mono<UserDTO>

    fun updateUser(id: Int, userDTO: UserDTO, authentication: Authentication): Mono<UserDTO>

    fun deleteUserById(id: Int, authentication: Authentication): Mono<Void>
}