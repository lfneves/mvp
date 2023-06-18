package com.mvp.delivery.delivery.infrastruture.entity.order

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("tb_order")
class OrderEntity {
    @Id
    var id: Int? = null

    @Column("id_client")
    var idClient: Int = -1

    @Column("id_item")
    var idItem: Int = -1

    @Column("total_price")
    var totalPrice: BigDecimal = BigDecimal.ZERO

    var checkout: Boolean = false
}
