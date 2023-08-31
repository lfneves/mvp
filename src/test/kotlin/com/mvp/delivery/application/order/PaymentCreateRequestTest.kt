package com.mvp.delivery.application.order

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
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PaymentCreateRequestTest {

    @Test
    fun main() {
        try {
        MercadoPagoConfig.setAccessToken("TEST-1991344535865578-082721-1c19afb1a1f17e49919bd405c04621a9-170225675")



        val client = PaymentClient()
        //("5031433215406351", "1463204867-CbHemmVpmK3TaA", "123")
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