package com.mvp.delivery.domain.admin.user

import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.domain.client.model.user.UsernameDTO
import com.mvp.delivery.infrastruture.entity.user.UserEntity
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserAdminService {

    fun getUsers(): Flux<UserDTO>

    fun deleteAllUsers(): Mono<Void>
}