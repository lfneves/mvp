package com.mvp.delivery.domain.service.client.order

import com.mvp.delivery.domain.model.order.*
import com.mvp.delivery.domain.model.product.ProductRemoveOrderDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

interface OrderService {

    fun getOrderById(id: Long, authentication: Authentication): Mono<OrderByIdResponseDTO>

    suspend fun getOrderByExternalId(externalId: UUID): OrderByIdResponseDTO?

    fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO>

    fun updateOrderProduct(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO>

    fun deleteOrderById(id: Long, authentication: Authentication): Mono<Void>

    fun deleteOrderProductById(products: ProductRemoveOrderDTO, authentication: Authentication): Mono<Void>

    fun saveInitialOrders(order: OrderDTO, authentication: Authentication): Mono<OrderDTO>

    fun getAllOrderProductsByIdOrder(id: Long, authentication: Authentication): Flux<OrderProductResponseDTO>

    fun fakeCheckoutOrder(orderCheckoutDTO: OrderCheckoutDTO, authentication: Authentication): Mono<Boolean>

}