package com.mvp.delivery.infrastruture.entity.order

import com.mvp.delivery.domain.model.order.OrderProductDTO
import com.mvp.delivery.domain.model.order.OrderProductResponseDTO
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

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

data class OrderProductResponseEntity(
    var id: Long? = null,
    var idProduct: Long? = null,
    var idOrder: Long? = null,
    var productName: String? = null,
    var categoryName: String? = null,
    var price: BigDecimal = BigDecimal.ZERO
) {
    fun toDTO() : OrderProductResponseDTO {
        return OrderProductResponseDTO(
            id = this.id,
            idProduct = this.idProduct,
            idOrder = this.idOrder,
            productName = this.productName,
            categoryName = this.categoryName,
            price = this.price
        )
    }
}