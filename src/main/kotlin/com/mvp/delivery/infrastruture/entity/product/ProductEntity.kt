package com.mvp.delivery.infrastruture.entity.product

import com.mvp.delivery.domain.client.model.product.CategoryDTO
import com.mvp.delivery.domain.client.model.product.ProductDTO
import jakarta.persistence.CascadeType
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import reactor.core.publisher.Mono
import java.math.BigDecimal


@Table("tb_product")
data class ProductEntity(
    @Id
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = [CascadeType.ALL], orphanRemoval=true)
    var id: Long? = null,
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var quantity: Int = 0,

    @Column("id_category")
    var idCategory: Long? = null
) {
    fun toDTO(): ProductDTO {
        return ProductDTO(
            id = this.id,
            name = this.name,
            price = this.price,
            quantity = this.quantity,
            idCategory = this.idCategory ?: 0
        )
    }

    fun toDTO(category: CategoryDTO?): ProductDTO {
        return ProductDTO(
            id = this.id,
            name = this.name,
            price = this.price,
            quantity = this.quantity,
            idCategory = this.idCategory!!,
            category = category!!
        )
    }

    fun updateUserEntity(productEntity: ProductEntity, request: ProductEntity) {
        request.id?.let { productEntity.id = it }
        request.name?.let { productEntity.name = it }
        request.price?.let { productEntity.price = it }
        request.quantity?.let { productEntity.quantity = it }
    }

    fun toListDTO(listEntity: List<ProductEntity>): List<ProductDTO> {
        var listDTO: MutableList<ProductDTO> = mutableListOf()
        listEntity.forEach {
            listDTO.add(ProductDTO(
                    id = it.id,
                    name = it.name,
                    price = it.price,
                    quantity = it.quantity,
                    idCategory = it.idCategory!!
                )
            )
        }
        return listDTO.toList()
    }
}
