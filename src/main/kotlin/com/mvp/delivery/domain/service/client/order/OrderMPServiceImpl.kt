package com.mvp.delivery.domain.service.client.order

import com.mvp.delivery.domain.configuration.OrderEndpointPropertyConfiguration
import com.mvp.delivery.domain.configuration.OrderPropertyConfiguration
import com.mvp.delivery.domain.model.order.store.QrDataDTO
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class OrderMPServiceImpl(
    private val orderPropertyConfiguration: OrderPropertyConfiguration,
    private val orderEndpointPropertyConfiguration: OrderEndpointPropertyConfiguration
): OrderMPService {

    override fun generateOrderQrs(requestBody: String): Mono<QrDataDTO> {
        val client = WebClient.create()
        val endpoint = orderEndpointPropertyConfiguration.qrs.replace("?", orderPropertyConfiguration.userId)
        val requestUrl = orderPropertyConfiguration.url + endpoint
        val responseSpec = client.put()
            .uri(requestUrl)
            .header("Authorization", orderPropertyConfiguration.token)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(QrDataDTO::class.java)

        return responseSpec
    }
}