package com.mvp.delivery.domain.model.product

data class ProductRemoveOrderDTO(
    var orderProductId: MutableList<Long> = mutableListOf()
)
