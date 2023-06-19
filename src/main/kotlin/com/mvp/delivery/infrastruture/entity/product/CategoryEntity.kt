package com.mvp.delivery.infrastruture.entity.product

import com.mvp.delivery.domain.client.model.product.CategoryDTO
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("tb_category")
data class CategoryEntity(
    @Id
    var id: Long? = null,
    var name: String = "",
    var description: String = ""
) {
    fun toDTO(): CategoryDTO {
        return CategoryDTO(
            id = this.id,
            name = this.name,
            description = this.description
        )
    }
}