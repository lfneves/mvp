package com.mvp.delivery.application.order

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mvp.delivery.domain.model.order.payment.CashOutDTO
import com.mvp.delivery.domain.model.order.payment.ItemDTO
import com.mvp.delivery.domain.model.order.payment.OrderQrsDTO
import com.mvp.delivery.domain.model.order.payment.SponsorDTO
import com.mvp.delivery.helpers.OrderMock
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class OrderMPQrsTest {

    @Value("\${order.mp-api.user-id}")
    private val userID: Long? = null

    @Value("\${order.mp-api.url")
    private val url: String? = null

    @Value("\${order.mp-api.api-endpoints.qrs")
    private val apiEndpointQrs: String? = null

    @Value("\${order.notification-url")
    private val notificationUrl: String? = null

    @Value("\${order.mp.sponsor-id")
    private val sponsorID: String? = null

    @Test
    fun orderQrsCreateData() {
        try {
            val mapper = jacksonObjectMapper()
            val orderQrsDTO =  OrderQrsDTO()

            orderQrsDTO.externalReference = "123"
            orderQrsDTO.title = "title"
            orderQrsDTO.notificationUrl = "https://webhook.site/3a01d6b2-7c0c-4c45-a5c7-5b305a11a3a8"
            orderQrsDTO.totalAmount = 2
            orderQrsDTO.description = "description"

            val item = ItemDTO(
                category = "Test",
                description = "Test",
                quantity = 1,
                sku_number = "TesteSku",
                title = "Test Item",
                total_amount = 1,
                unit_measure = "unit",
                unit_price = 1
            )
            val sponsor = SponsorDTO(id = 57174696)
            val cashOut = CashOutDTO(amount = 1)
            orderQrsDTO.itemDTOS = listOf(item)
            orderQrsDTO.sponsorDTO = sponsor
            orderQrsDTO.cashOut = cashOut

            println(mapper.writeValueAsString(orderQrsDTO))

            val client = WebClient.create()
            val responseSpec = client.put()
                .uri("https://api.mercadopago.com/instore/orders/qr/seller/collectors/170225675/pos/170225675/qrs")
                .header("Authorization", OrderMock.mockOrder().tokenMP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(orderQrsDTO))
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

            println(responseSpec)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun mockJsonQrs() {
        try {
            val fileContent = this::class.java.classLoader.getResource("MockJsonQrs.json")?.readText()

            val client = WebClient.create()
            val responseSpec = client.put()
                .uri("https://api.mercadopago.com/instore/orders/qr/seller/collectors/170225675/pos/170225675/qrs")
                .header("Authorization", OrderMock.mockOrder().tokenMP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(fileContent!!)
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

            println(responseSpec)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}