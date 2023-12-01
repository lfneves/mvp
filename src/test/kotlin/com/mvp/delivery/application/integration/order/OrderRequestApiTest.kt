package com.mvp.delivery.application.integration.order

import com.mvp.delivery.domain.model.order.OrderDTO
import com.mvp.delivery.domain.model.order.OrderRequestDTO
import com.mvp.delivery.domain.model.order.OrderResponseDTO
import com.mvp.delivery.helpers.OrderMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

class OrderRequestApiTest(
    @Autowired val client: WebTestClient,
) {
    fun createOrderTest(request: OrderRequestDTO = OrderMock.mockOrderRequest()): OrderResponseDTO = client.post()
        .uri("/api/v1/order/create-order")
        .bodyValue(request)
        .exchange()
        .expectBody<OrderResponseDTO>()
        .returnResult()
        .responseBody!!

    fun getAll(): OrderDTO = client.get()
        .uri("/api/v1/order")
        .exchange()
        .expectBody<OrderDTO>()
        .returnResult()
        .responseBody!!

    fun getById(id: Int): OrderDTO = client.get()
        .uri("/api/v1/order/all-products-by-order-id/$id")
        .exchange()
        .expectBody<OrderDTO>()
        .returnResult()
        .responseBody!!

    fun deleteOrderById(id: Int): OrderDTO = client.delete()
        .uri("/api/v1/order/$id")
        .exchange()
        .expectBody<OrderDTO>()
        .returnResult()
        .responseBody!!
}