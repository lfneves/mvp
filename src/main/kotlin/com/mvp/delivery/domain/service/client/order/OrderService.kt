package com.mvp.delivery.domain.service.client.order

import com.mvp.delivery.domain.model.order.*
import com.mvp.delivery.domain.model.product.ProductRemoveOrderDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderService {

    fun getOrderById(id: Int, authentication: Authentication): Mono<OrderByIdResponseDTO>

    fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO>

    fun updateOrderProduct(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO>

    fun deleteOrderById(id: Int, authentication: Authentication): Mono<Void>

    fun deleteOrderProductById(products: ProductRemoveOrderDTO, authentication: Authentication): Mono<Void>

    fun saveInitialOrders(order: OrderDTO, authentication: Authentication): Mono<OrderDTO>

    fun getAllOrderProductsByIdOrder(id: Long, authentication: Authentication): Flux<OrderProductResponseDTO>

    fun checkoutOrder(orderCheckoutDTO: OrderCheckoutDTO, authentication: Authentication): Mono<String>
}