package com.mvp.delivery.domain.service.admin.user

import com.mvp.delivery.domain.model.user.UserDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserAdminService {

    fun getUsers(): Flux<UserDTO>

    fun deleteAllUsers(): Mono<Void>
}