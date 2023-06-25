package com.mvp.delivery.infrastruture.entity.order

import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tb_order_product")
data class OrderProductEntity(
    @Id @Column("id")
    var id: Long? = null,
    @Column("id_product")
    var idProduct: Long? = null,
    @Column("id_order")
    var idOrder: Long? = null
) {
    fun toDTO() : OrderProductDTO {
        return OrderProductDTO(
            id = this.id,
            idProduct = this.idProduct,
            idOrder = this.idOrder
        )
    }
}