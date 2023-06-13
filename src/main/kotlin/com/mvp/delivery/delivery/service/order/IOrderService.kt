package com.mvp.delivery.delivery.service.order

import com.mvp.delivery.delivery.model.Order
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IOrderService {
    fun getOrderById(id: Int): Mono<Order>
    fun saveOrder(Order: Order): Mono<Order>
    fun updateOrder(id: Int, Order: Order): Mono<Order>
    fun deleteOrderById(id: Int): Mono<Void>
    fun getOrders(): Flux<Order>
    fun deleteAllOrders(): Mono<Void>
    fun saveInitialOrder(Order: Order): Mono<Order>
}