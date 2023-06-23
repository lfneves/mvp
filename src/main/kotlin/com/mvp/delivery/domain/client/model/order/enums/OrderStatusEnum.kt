package com.mvp.delivery.domain.client.model.order.enums

enum class OrderStatusEnum(val value: String) {
    PENDING("Pendente"),
    PREPARING("Preparando"),
    PAID("Pago"),
    FINISHED("Finalizado"),
}