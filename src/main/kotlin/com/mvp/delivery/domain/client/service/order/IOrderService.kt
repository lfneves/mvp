package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.order.OrderDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IOrderService {
    fun getOrderById(id: Int): Mono<OrderDTO>
    fun saveOrder(order: OrderDTO): Mono<OrderDTO>
    fun updateOrder(id: Int, order: OrderDTO): Mono<OrderDTO>
    fun deleteOrderById(id: Int): Mono<Void>
    fun getOrders(): Flux<OrderDTO>
    fun deleteAllOrders(): Mono<Void>
    fun saveInitialOrder(order: OrderDTO): Mono<OrderDTO>
}