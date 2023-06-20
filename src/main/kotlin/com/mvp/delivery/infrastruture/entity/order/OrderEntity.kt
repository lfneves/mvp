package com.mvp.delivery.infrastruture.entity.order

import com.mvp.delivery.domain.client.model.order.OrderDTO
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("tb_order")
data class OrderEntity (
    @Id var id: Int? = null,
    @Column("id_client") var idClient: Int = -1,
    @Column("total_price") var totalPrice: BigDecimal = BigDecimal.ZERO,
    var checkout: Boolean = false
) {
    fun toDTO(): OrderDTO {
        return OrderDTO(
            id = this.id,
            idClient = this.idClient,
            totalPrice = this.totalPrice,
            checkout = this.checkout
        )
    }
}