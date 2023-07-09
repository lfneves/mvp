package com.mvp.delivery.infrastruture.entity.product

import com.mvp.delivery.domain.model.product.CategoryDTO
import jakarta.persistence.CascadeType
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("tb_category")
data class CategoryEntity(
    @Id
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = [CascadeType.ALL], orphanRemoval=true)
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