package com.mvp.delivery.domain.service.admin.order

import com.mvp.delivery.domain.model.order.OrderDTO
import com.mvp.delivery.domain.model.order.OrderFinishDTO
import com.mvp.delivery.domain.model.order.OrderStatusDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderAdminService {

    fun updateOrderStatus(id: Int, orderStatusDTO: OrderStatusDTO, authentication: Authentication): Mono<OrderDTO>

    fun getOrders(): Flux<OrderDTO>

    fun deleteAllOrders(): Mono<Void>

    fun updateOrderFinishedAndStatus(orderFinishDTO: OrderFinishDTO, authentication: Authentication): Mono<OrderDTO>
}