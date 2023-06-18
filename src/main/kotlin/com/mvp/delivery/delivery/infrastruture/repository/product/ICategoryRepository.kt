package com.mvp.delivery.delivery.infrastruture.repository.product

import com.mvp.delivery.delivery.infrastruture.entity.product.CategoryEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ICategoryRepository : ReactiveCrudRepository<CategoryEntity?, Long?> {
    @Query("select id, name, description from tb_category where name = $1")
    fun findByName(name: String?): Flux<CategoryEntity>
    fun findById(id: Int): Mono<CategoryEntity>
    fun deleteById(id: Int): Mono<Void>
}