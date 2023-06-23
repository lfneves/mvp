package com.mvp.delivery.infrastruture.repository.order

import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface OrderProductRepository : ReactiveCrudRepository<OrderProductEntity?, Long?> {

    @Modifying
    @Query("SELECT id, id_order, id_product FROM tb_order_product WHERE id_order = $1")
    fun findAllByIdOrder(id: Long): Flux<OrderProductEntity>
}