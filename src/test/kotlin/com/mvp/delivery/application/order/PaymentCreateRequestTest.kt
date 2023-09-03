package com.mvp.delivery.application.order

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.cardtoken.CardTokenClient
import com.mercadopago.client.cardtoken.CardTokenRequest
import com.mercadopago.client.common.IdentificationRequest
import com.mercadopago.client.customer.CustomerClient
import com.mercadopago.client.merchantorder.MerchantOrderClient
import com.mercadopago.client.payment.PaymentClient
import com.mercadopago.client.payment.PaymentCreateRequest
import com.mercadopago.client.payment.PaymentPayerRequest
import com.mercadopago.exceptions.MPApiException
import com.mercadopago.exceptions.MPException
import com.mvp.delivery.domain.model.order.store.webhook.MerchantOrderResponseDTO
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.math.BigDecimal
import java.time.Duration
import java.util.function.Function

class PaymentCreateRequestTest {
    private val TEST_MP_TOKEN = "Bearer TEST-29597298295284-083000-fcac7f6198671d6a6b16cdeecc34c787-170225675"

    @Test
    fun getMerchantOrderTest() {
        val mapper = jacksonObjectMapper()
        val requestUrl = "https://api.mercadolibre.com/merchant_orders/11511214319"
        try {

            val client = WebClient.create()
            val response = client.get()
                .uri(requestUrl)
                .header("Authorization", TEST_MP_TOKEN)
                .retrieve()
                .bodyToMono(MerchantOrderResponseDTO::class.java)
//                .flatMap { t -> Mono.just(t) }

            val test = response
            println(mapper.writeValueAsString(test))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun paymentTest() {
        try {
        MercadoPagoConfig.setAccessToken("TEST-1991344535865578-082721-1c19afb1a1f17e49919bd405c04621a9-170225675")

        val client = PaymentClient()
        val customer = CustomerClient().get("1463204867-CbHemmVpmK3TaA")
        val cardTokenRequest = CardTokenRequest.builder()
            .cardId("4235647728025682")
            .customerId(customer.id)
            .securityCode("123")
            .build()
        val cardTokenClient = CardTokenClient().create(cardTokenRequest)
        val createRequest = PaymentCreateRequest.builder()
            .transactionAmount(BigDecimal(1000))
            .token(cardTokenClient.id)
            .description("Test01")
            .installments(1)
            .paymentMethodId("visa")
            .payer(PaymentPayerRequest.builder().email("test01@delivery.com").build())
            .build()

            val payment = client.create(createRequest)
            println(payment)
        } catch (ex: MPApiException) {
            System.out.printf(
                "MercadoPago Error. Status: %s, Content: %s%n",
                ex.apiResponse.statusCode, ex.apiResponse.content
            )
        } catch (ex: MPException) {
            ex.printStackTrace()
        }
    }
}