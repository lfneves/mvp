package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import com.mvp.delivery.domain.client.model.product.ProductRemoveOrderDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IOrderService {
    fun getOrderById(id: Int): Mono<OrderDTO>

    fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO>

    fun updateOrderProduct(id: Int, order: OrderDTO): OrderDTO

    fun updateOrderStatus(id: Int, orderStatusDTO: OrderStatusDTO): Mono<OrderDTO>

    fun deleteOrderById(id: Int): Mono<Void>

    fun deleteOrderProductById(products: ProductRemoveOrderDTO): Mono<Void>

    fun getOrders(): Flux<OrderDTO>

    fun deleteAllOrders(): Mono<Void>

    fun saveInitialOrder(order: OrderDTO): Mono<OrderDTO>

    fun getAllOrderProductsByIdOrder(id: Long): Flux<OrderProductDTO>
}