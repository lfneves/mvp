package com.mvp.delivery.domain.model.product

import com.mvp.delivery.infrastruture.entity.product.CategoryEntity
import io.swagger.v3.oas.annotations.media.Schema

data class CategoryDTO(
    @Schema(hidden = true)
    var id: Long? = null,
    var name: String = "",
    var description: String = ""
) {
    fun toEntity(): CategoryEntity {
        return CategoryEntity(
            id = this.id,
            name = this.name,
            description = this.description
        )
    }
}