package com.mvp.delivery.domain.client.model.product

data class ProductRemoveOrderDTO(
    var productId: MutableList<Long> = mutableListOf()
)
