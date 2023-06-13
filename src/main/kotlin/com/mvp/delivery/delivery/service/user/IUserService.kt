package com.mvp.delivery.delivery.service.user

import com.mvp.delivery.delivery.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IUserService {
    fun getUserById(id: Int): Mono<User>
    fun saveUser(user: User): Mono<User>
    fun signup(user: User): Mono<User>
    fun updateUser(id: Int, user: User): Mono<User>
    fun deleteUser(id: Int): Mono<Void>
    fun getUsers(): Flux<User>
    fun deleteAllUsers(): Mono<Void>
    fun saveInitialUser(user: User): Mono<User>
}