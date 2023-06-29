package com.mvp.delivery.domain.client.model.order

import org.jetbrains.annotations.NotNull

data class OrderFinishDTO(
    @NotNull
    var idOrder: Long = 0,
    var isFinished: Boolean = false
)

data class OrderCheckoutDTO(
    var isPayment: Boolean = false
)