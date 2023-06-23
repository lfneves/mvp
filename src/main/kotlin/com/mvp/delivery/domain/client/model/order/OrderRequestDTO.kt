package com.mvp.delivery.domain.client.model.order

import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import reactor.core.publisher.Flux

data class OrderRequestDTO(
    val username: String,
    var orderProduct : MutableList<OrderProductDTO>
) {
    fun toEntityList(): MutableList<OrderProductEntity> {
        return this.orderProduct.map {
            return@map OrderProductEntity(
                id = it.id,
                idProduct = it.idProduct,
                idOrder = it.idOrder
            )
        }.toMutableList()
    }
}