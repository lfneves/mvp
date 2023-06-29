package com.mvp.delivery.infrastruture.repository.order

import com.mvp.delivery.domain.client.model.order.enums.OrderStatusEnum
import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import com.mvp.delivery.infrastruture.entity.product.ProductEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderRepository : ReactiveCrudRepository<OrderEntity?, Long?> {

    @Query("""
        SELECT tb_order.id, id_client, SUM(price) AS total_price, status, is_finished
         FROM tb_order 
         INNER JOIN tb_client ON tb_client.id = tb_order.id_client
         INNER JOIN tb_order_product ON tb_order_product.id_order = tb_order.id
         INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
         WHERE tb_client.cpf = $1
         GROUP BY tb_order.id, id_client, status, is_finished
    """)
    fun findByUsername(username: String?): Mono<OrderEntity>

    @Query("""SELECT id, id_client, total_price, status, is_finished
            FROM tb_order
            WHERE id_client = :idUser
            AND is_finished = false 
            AND status = :status """)
    fun findOrderByIdUserAndStatusNotNull(idUser: Int, status: String = OrderStatusEnum.PENDING.value): Mono<OrderEntity>

    @Query("""INSERT INTO 
        tb_order (id_client, total_price, status, is_finished) 
        VALUES(id_client, total_price, status, is_finished)
        SELECT :#{#orderEntity.id_client}, SUM(price) AS total_price, :#{#orderEntity.status}, :#{#orderEntity.is_finished}
        FROM tb_order 
        INNER JOIN tb_client ON tb_client.id = tb_order.id_client
        INNER JOIN tb_order_product ON tb_order_product.id_order = tb_order.id
        INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
        WHERE tb_client.cpf = :username
        GROUP BY tb_order.id, id_client, status, is_finished
        
    """)
    fun saveFirstOrder(username: String?, orderEntity: OrderEntity): Mono<ProductEntity>

    @Query("""
        SELECT tb_order.id, id_client, SUM(price) AS total_price, status, is_finished
         FROM tb_order 
         INNER JOIN tb_client ON tb_client.id = tb_order.id_client
         INNER JOIN tb_order_product ON tb_order_product.id_order = tb_order.id
         INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
         WHERE tb_order.id = $1
         GROUP BY tb_order.id, id_client, status, is_finished
    """)
    fun findById(id: Int): Mono<OrderEntity>

    fun deleteById(id: Int): Mono<Void>

    @Query("""
        SELECT tb_order.id, id_client, SUM(price) AS total_price, status, is_finished
         FROM tb_order 
         INNER JOIN tb_client ON tb_client.id = tb_order.id_client
         INNER JOIN tb_order_product ON tb_order_product.id_order = tb_order.id
         INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
         GROUP BY tb_order.id, id_client, status, is_finished
    """)
    fun findAllOrder(): Flux<OrderEntity>
}