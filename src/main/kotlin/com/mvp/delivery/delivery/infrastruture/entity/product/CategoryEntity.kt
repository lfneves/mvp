package com.mvp.delivery.delivery.infrastruture.entity.product

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("tb_category")
data class CategoryEntity(
    @Id
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null
)