package com.mvp.delivery.domain.client.model.product

data class ProductRemoveOrderDTO(
    var orderProductId: MutableList<Long> = mutableListOf()
)
