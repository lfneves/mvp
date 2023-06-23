package com.mvp.delivery.domain.client.model.order

import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import java.math.BigDecimal

data class OrderDTO(
    var id: Long? = null,
    var idClient: Int? = null,
    var totalPrice: BigDecimal = BigDecimal.ZERO,
    var status: String = "",
    var isFinished: Boolean = false,
    var orderProduct : MutableList<OrderProductDTO> = mutableListOf()
) {
    fun toEntity(): OrderEntity{
        return OrderEntity(
            id = this.id,
            idClient = this.idClient,
            totalPrice = this.totalPrice,
            isFinished = this.isFinished
        )
    }
}