package com.mvp.delivery.infrastruture.entity.order

import java.math.BigDecimal

data class OrderProductEntity(
    var idOrderProduct: Int? = null,
    var idProduct: Int = -1,
    var idOrder: BigDecimal = BigDecimal.ZERO
)