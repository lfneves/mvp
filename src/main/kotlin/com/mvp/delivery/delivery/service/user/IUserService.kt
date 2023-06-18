package com.mvp.delivery.delivery.service.user

import com.mvp.delivery.delivery.model.UserVO
import com.mvp.delivery.delivery.model.entity.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IUserService {
    fun getUserById(id: Int): Mono<UserVO>
    fun saveUser(user: User): Mono<User>
    fun signup(user: UserVO): Mono<User>
    fun updateUser(id: Int, userVO: UserVO): Mono<UserVO>
    fun deleteUser(id: Int): Mono<Void>
    fun getUsers(): Flux<UserVO>
    fun deleteAllUsers(): Mono<Void>
    fun saveInitialUser(user: User): Mono<User>
}