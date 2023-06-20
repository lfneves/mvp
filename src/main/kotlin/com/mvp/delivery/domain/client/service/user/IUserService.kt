package com.mvp.delivery.domain.client.service.user

import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.infrastruture.entity.user.UserEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IUserService {
    fun getUserById(id: Int): Mono<UserDTO>
    fun saveUser(userEntity: UserEntity): Mono<UserEntity>
    fun signup(user: UserDTO): Mono<UserDTO>
    fun updateUser(id: Int, userDTO: UserDTO): Mono<UserDTO>
    fun deleteUser(id: Int): Mono<Void>
    fun getUsers(): Flux<UserDTO>
    fun deleteAllUsers(): Mono<Void>
    fun saveInitialUser(userEntity: UserEntity): Mono<UserEntity>
}