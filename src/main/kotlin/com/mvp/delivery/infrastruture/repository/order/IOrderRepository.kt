package com.mvp.delivery.infrastruture.repository.order

import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IOrderRepository : ReactiveCrudRepository<OrderEntity?, Long?> {
    @Query("select id, id_client, id_item, total_price, checkout from tb_order where name = $1")
    fun findByName(name: String?): Flux<OrderEntity>
    fun findById(id: Int): Mono<OrderEntity>
    fun deleteById(id: Int): Mono<Void>
}