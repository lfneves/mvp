package com.mvp.delivery.infrastruture.entity.order

import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.math.BigDecimal

data class OrderProductEntity(
    @Id @Column("id_order_product") var idOrderProduct: Int? = null,
    @Column("id_product") var idProduct: Int = -1,
    @Column("id_order") var idOrder: BigDecimal = BigDecimal.ZERO
) {
    fun toDTO() : OrderProductDTO{
        return OrderProductDTO(
            idOrderProduct = this.idOrderProduct,
            idProduct = this.idProduct,
            idOrder = this.idOrder
        )
    }
}