package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.order.OrderDTO
import com.mvp.delivery.domain.client.model.order.OrderRequestDTO
import com.mvp.delivery.domain.client.model.order.OrderResponseDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IOrderService {
    fun getOrderById(id: Int): Mono<OrderDTO>

    fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Flux<OrderResponseDTO>

    suspend fun updateOrder(id: Int, order: OrderDTO): OrderDTO

    fun deleteOrderById(id: Int): Mono<Void>

    fun getOrders(): Flux<OrderDTO>

    fun deleteAllOrders(): Mono<Void>

    fun saveInitialOrder(order: OrderDTO): Mono<OrderDTO>
}