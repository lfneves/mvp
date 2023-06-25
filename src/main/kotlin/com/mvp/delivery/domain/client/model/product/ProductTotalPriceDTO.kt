package com.mvp.delivery.domain.client.model.product

import java.math.BigDecimal

data class ProductTotalPriceDTO(
    var totalPrice: BigDecimal = BigDecimal.ZERO
)