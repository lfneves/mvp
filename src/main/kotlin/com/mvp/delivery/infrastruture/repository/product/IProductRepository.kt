package com.mvp.delivery.infrastruture.repository.product

import com.mvp.delivery.infrastruture.entity.product.ProductEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IProductRepository : ReactiveCrudRepository<ProductEntity?, Long?> {
    @Query("select id, name, price, quantity from tb_product where name = $1")
    fun findByName(name: String?): Flux<ProductEntity>
    @Query("select id, name, price, quantity, id_category from tb_product where id_category = $1")
    fun findByIdCategory(name: Long?): Flux<ProductEntity>
    fun findById(id: Int): Mono<ProductEntity>
    fun deleteById(id: Int): Mono<Void>
}