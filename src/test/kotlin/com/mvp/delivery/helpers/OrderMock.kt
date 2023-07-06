package com.mvp.delivery.helpers

import com.mvp.delivery.domain.client.model.order.OrderDTO
import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import com.mvp.delivery.domain.client.model.order.OrderRequestDTO
import java.math.BigDecimal

object OrderMock {
    private const val TEST_USERNAME = "12345678912"
    private const val TEST_PRODUCT_ID = 1L
    private const val TEST_CLIENT_ID = 1
    private val TEST_TOTAL_PRICE = BigDecimal.TEN
    private const val TEST_STATUS = "Pendente"
    private const val TEST_FINISHED_TRUE = true
    private const val TEST_FINISHED_FALSE = false

    fun mockOrderRequest() = OrderRequestDTO(
        orderProduct = mutableListOf(
            OrderProductDTO(
                idProduct = TEST_PRODUCT_ID
            )
        )
    )

    fun mockOrder() = OrderDTO(
        idClient = TEST_CLIENT_ID,
        totalPrice = TEST_TOTAL_PRICE,
        status = TEST_STATUS,
        isFinished = TEST_FINISHED_FALSE
    )
}