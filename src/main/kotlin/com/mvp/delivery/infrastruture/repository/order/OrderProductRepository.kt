package com.mvp.delivery.infrastruture.repository.order

import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import com.mvp.delivery.infrastruture.entity.order.OrderProductResponseEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderProductRepository : ReactiveCrudRepository<OrderProductEntity?, Long?> {

    @Query("""
        SELECT tb_order_product.id, id_product, id_order, tb_product.name, tb_product.price
        FROM tb_order_product
        INNER JOIN tb_order ON tb_order.id = tb_order_product.id_order
        INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
        WHERE tb_order_product.id_order = :id
    """)
    fun findAllByIdOrderInfo(id: Long): Flux<OrderProductResponseEntity>

    fun findAllByIdOrder(id: Long): Flux<OrderProductEntity>

    fun deleteByIdOrder(id: Long): Mono<Void>

    @Query("""
        DELETE FROM tb_order_product WHERE id_product IN (:ids)
    """)
    fun deleteByIdProduct(ids: List<Long>): Mono<Void>
}