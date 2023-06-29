package com.mvp.delivery.domain.client.model.order

import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity

data class OrderRequestDTO(
    var orderProduct : List<OrderProductDTO>
) {
    fun toEntityList(): List<OrderProductEntity> {
        return this.orderProduct.map {
            return@map OrderProductEntity(
                id = it.id,
                idProduct = it.idProduct,
                idOrder = it.idOrder
            )
        }
    }
}