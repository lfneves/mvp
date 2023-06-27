package com.mvp.delivery.infrastruture.entity.order

import com.mvp.delivery.domain.client.model.order.OrderDTO
import jakarta.persistence.CascadeType
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("tb_order")
data class OrderEntity (
    @Id
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = [CascadeType.PERSIST])
    var id: Long? = null,
    @Column("id_client") var idClient: Int? = null,
    @Column("total_price") var totalPrice: BigDecimal = BigDecimal.ZERO,
    @Column("status") var status: String = "",
    @Column("is_finished") var isFinished: Boolean = false
) {
    fun toDTO(): OrderDTO {
        return OrderDTO(
            id = this.id,
            idClient = this.idClient,
            totalPrice = this.totalPrice,
            status = this.status,
            isFinished = this.isFinished
        )
    }
}