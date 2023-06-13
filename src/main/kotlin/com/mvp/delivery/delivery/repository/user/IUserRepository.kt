package com.mvp.delivery.delivery.repository.user


import com.mvp.delivery.delivery.model.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IUserRepository : ReactiveCrudRepository<User?, Long?> {
    @Query("select id, password, name, email, cpf from tb_client where name = $1")
    fun findByName(name: String?): Flux<User>
    fun findById(id: Int): Mono<User>
    fun deleteById(id: Int): Mono<Void>
    @Query("SELECT * FROM tb_client u JOIN address a ON u.address_id = a.id WHERE u.id = $1")
    fun findByIdWithAddress(id: Int): Mono<User>
}