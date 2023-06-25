package com.mvp.delivery.infrastruture.repository.order

import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderProductRepository : ReactiveCrudRepository<OrderProductEntity?, Long?> {

    fun findAllByIdOrder(id: Long): Flux<OrderProductEntity>

    fun deleteByIdOrder(id: Long): Mono<Void>

    @Query("""
        DELETE FROM tb_order_product WHERE id_product IN (:ids)
    """)
    fun deleteByIdProduct(ids: List<Long>): Mono<Void>
}