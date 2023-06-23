package com.mvp.delivery.infrastruture.repository.order

import com.mvp.delivery.domain.client.model.order.enums.OrderStatusEnum
import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IOrderRepository : ReactiveCrudRepository<OrderEntity?, Long?> {

    @Query("""
        SELECT tb_order.id, id_client, total_price, status, is_finished
         FROM tb_order 
         INNER JOIN tb_client ON tb_client.id = tb_order.id_client
         WHERE tb_client.cpf = $1
    """)
    fun findByUsername(name: String?): Flux<OrderEntity>

    @Query("""SELECT id, id_client, total_price, status, is_finished
            FROM tb_order
            WHERE id_client = :idUser
            AND is_finished = false 
            AND status = :status """)
    fun findOrderByIdUserAndStatusNotNull(idUser: Int, status: String = OrderStatusEnum.PENDING.value): Mono<OrderEntity>

    @Modifying
    @Query("""
        SELECT EXISTS (
            SELECT id FROM tb_order WHERE id_client = :idClient AND is_finished = false AND status = :status
        ) AS result """)
    fun existsOrderUser(idClient: Int, status: String = OrderStatusEnum.PENDING.value): Mono<Boolean>

    fun existsByIdClient(idClient: Int): Mono<Boolean>

    fun findById(id: Int): Mono<OrderEntity>

    fun findByIdClient(id: Int): Flux<OrderEntity>

    fun deleteById(id: Int): Mono<Void>
}