package com.mvp.delivery.helpers

import com.mvp.delivery.domain.client.model.order.OrderDTO
import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import com.mvp.delivery.domain.client.model.order.OrderRequestDTO
import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.utils.Sha512PasswordEncoder
import java.math.BigDecimal

object OrderMock {
    private const val TEST_USERNAME = "123456789000"
    private const val TEST_NAME = "Lucas"
    private const val TEST_EMAIL = "testemail@email.com"
    private const val TEST_PASSWORD = "testpassword"
    private const val TEST_PRODUCT_ID = 1L
    private const val TEST_CLIENT_ID = 1
    private val TEST_TOTAL_PRICE = BigDecimal.TEN
    private const val TEST_FINISHED_TRUE = true
    private const val TEST_FINISHED_FALSE = false

    fun mockOrderRequest() = OrderRequestDTO(
        username = TEST_USERNAME,
        orderProduct = mutableListOf(
            OrderProductDTO(
                idProduct = TEST_PRODUCT_ID
            )
        )
    )

    fun mockOrder() = OrderDTO(
        idClient = TEST_CLIENT_ID,
        totalPrice = TEST_TOTAL_PRICE,
        isFinished = TEST_FINISHED_FALSE
    )
}