package com.mvp.delivery.delivery.repository.item


import com.mvp.delivery.delivery.model.Product
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IProductRepository : ReactiveCrudRepository<Product?, Long?> {
    @Query("select id, name, price, quantity from tb_product where name = $1")
    fun findByName(name: String?): Flux<Product>
    @Query("select id, name, price, quantity, id_category from tb_product where id_category = $1")
    fun findByIdCategory(name: Long?): Flux<Product>
    fun findById(id: Int): Mono<Product>
    fun deleteById(id: Int): Mono<Void>
}