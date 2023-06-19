package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IOrderService {
    fun getOrderById(id: Int): Mono<OrderEntity>
    fun saveOrder(orderEntity: OrderEntity): Mono<OrderEntity>
    fun updateOrder(id: Int, orderEntity: OrderEntity): Mono<OrderEntity>
    fun deleteOrderById(id: Int): Mono<Void>
    fun getOrders(): Flux<OrderEntity>
    fun deleteAllOrders(): Mono<Void>
    fun saveInitialOrder(orderEntity: OrderEntity): Mono<OrderEntity>
}