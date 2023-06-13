package com.mvp.delivery.delivery.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal


@Table("tb_product")
class Product(
    @Id
    var id: Int? = null,
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var quantity: Int = 0,

    @Column("id_category")
    var idCategory: Long? = null,

//    @Transient
    var category: Category? = null
)
