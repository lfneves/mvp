package com.mvp.delivery.delivery.repository.item

import com.mvp.delivery.delivery.model.Category
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ICategoryRepository : ReactiveCrudRepository<Category?, Long?> {
    @Query("select id, name, description from tb_category where name = $1")
    fun findByName(name: String?): Flux<Category>
    fun findById(id: Int): Mono<Category>
    fun deleteById(id: Int): Mono<Void>
}