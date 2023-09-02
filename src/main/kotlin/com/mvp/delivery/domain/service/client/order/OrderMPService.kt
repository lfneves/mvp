package com.mvp.delivery.domain.service.client.order

import com.mvp.delivery.domain.model.order.store.QrDataDTO
import reactor.core.publisher.Mono

interface OrderMPService {

    fun generateOrderQrs(requestBody: String): Mono<QrDataDTO>
}