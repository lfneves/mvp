package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import com.mvp.delivery.domain.client.model.product.ProductRemoveOrderDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderService {

    fun getOrderById(id: Int, authentication: Authentication): Mono<OrderDTO>

    fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO>

    fun updateOrderProduct(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO>

    fun updateOrderStatus(id: Int, orderStatusDTO: OrderStatusDTO, authentication: Authentication): Mono<OrderDTO>

    fun deleteOrderById(id: Int, authentication: Authentication): Mono<Void>

    fun deleteOrderProductById(products: ProductRemoveOrderDTO, authentication: Authentication): Mono<Void>

    fun saveInitialOrders(order: OrderDTO, authentication: Authentication): Mono<OrderDTO>

    fun getAllOrderProductsByIdOrder(id: Long, authentication: Authentication): Flux<OrderProductResponseDTO>

    fun getOrders(): Flux<OrderDTO>

    fun deleteAllOrders(): Mono<Void>

    fun updateOrderFinishedAndStatus(orderFinishDTO: OrderFinishDTO, authentication: Authentication): Mono<OrderDTO>
}