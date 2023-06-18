package com.mvp.delivery.delivery.infrastruture.entity.product

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal


@Table("tb_product")
data class ProductEntity(
    @Id
    var id: Int? = null,
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var quantity: Int = 0,

    @Column("id_category")
    var idCategory: Long? = null,

//    @Transient
    var categoryEntity: CategoryEntity? = null
)
