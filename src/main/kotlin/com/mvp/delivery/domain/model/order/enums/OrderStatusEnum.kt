package com.mvp.delivery.domain.model.order.enums

enum class OrderStatusEnum(val value: String) {
    PENDING("Pendente"),
    PAYMENT_REQUIRED("Aguardando Pagamento"),
    PREPARING("Preparando"),
    PAID("Pago"),
    FINISHED("Finalizado"),
}