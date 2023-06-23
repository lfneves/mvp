package com.mvp.delivery.domain.client.model.order

import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity

data class OrderProductDTO(
    var id: Long? = null,
    var idProduct: Long? = null,
    var idOrder: Long? = null
) {
    fun toEntity() : OrderProductEntity {
        return OrderProductEntity(
            id = this.id,
            idProduct = this.idProduct,
            idOrder = this.idOrder
        )
    }
}