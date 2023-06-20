package com.mvp.delivery.domain.client.model.order

import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import java.math.BigDecimal

data class OrderProductDTO(
    var idOrderProduct: Int? = null,
    var idProduct: Int = -1,
    var idOrder: BigDecimal = BigDecimal.ZERO
) {
    fun toEntity() : OrderProductEntity {
        return OrderProductEntity(
            idOrderProduct = this.idOrderProduct,
            idProduct = this.idProduct,
            idOrder = this.idOrder
        )
    }
}