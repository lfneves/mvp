package com.mvp.delivery.domain.client.model.order

import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import java.math.BigDecimal

data class OrderProductDTO(
    var id: Int? = null,
    var idClient: Int = -1,
    var totalPrice: BigDecimal = BigDecimal.ZERO,
    var checkout: Boolean = false
) {
    fun toEntity(): OrderEntity{
        return OrderEntity(
            id = this.id,
            idClient = this.idClient,
            totalPrice = this.totalPrice,
            checkout = this.checkout
        )
    }
}