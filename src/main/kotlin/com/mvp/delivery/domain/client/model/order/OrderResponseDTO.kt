package com.mvp.delivery.domain.client.model.order

import reactor.core.publisher.Flux

data class OrderResponseDTO(
    var orderDTO: OrderDTO? = null,
    var orderProduct: Flux<OrderProductDTO>? = null
)