package com.mvp.delivery.domain.model.order

import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import com.mvp.delivery.infrastruture.entity.order.OrderProductResponseEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

data class OrderByIdResponseDTO(
    var id: Long? = null,
    var externalId: UUID? = null,
    var idClient: Int? = null,
    var totalPrice: BigDecimal = BigDecimal.ZERO,
    var status: String = "",
    var waitingTime: LocalDateTime = ZonedDateTime.now(ZoneId.of( "America/Sao_Paulo")).toLocalDateTime(),
    var isFinished: Boolean = false,
    var products: MutableList<OrderProductResponseEntity> = mutableListOf()
) {
    fun toEntity(): OrderEntity {
        return OrderEntity(
            id = this.id,
            idClient = this.idClient,
            totalPrice = this.totalPrice,
            status = this.status,
            waitingTime = this.waitingTime,
            isFinished = this.isFinished
        )
    }
}