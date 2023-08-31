package com.mvp.delivery.domain.service.client.order

import reactor.core.publisher.Mono

interface OrderMPService {

    fun gerateOrderQrs(requestBody: String): Mono<String>
}