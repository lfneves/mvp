package com.mvp.delivery.domain.model.product

import java.math.BigDecimal

data class ProductTotalPriceDTO(
    var totalPrice: BigDecimal = BigDecimal.ZERO
)