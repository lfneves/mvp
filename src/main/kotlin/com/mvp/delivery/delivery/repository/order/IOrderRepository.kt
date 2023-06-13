package com.mvp.delivery.delivery.repository.order


import com.mvp.delivery.delivery.model.Order
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IOrderRepository : ReactiveCrudRepository<Order?, Long?> {
    @Query("select id, id_client, id_item, total_price, checkout from tb_order where name = $1")
    fun findByName(name: String?): Flux<Order>
    fun findById(id: Int): Mono<Order>
    fun deleteById(id: Int): Mono<Void>
}