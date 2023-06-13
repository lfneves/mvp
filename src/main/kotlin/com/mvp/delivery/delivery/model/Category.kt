package com.mvp.delivery.delivery.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("tb_category")
data class Category(
    @Id
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null
)