package com.mvp.delivery.utils.constants

class ErrorMsgConstants {

    companion object {
        const val ERROR_CREATE_ORDER_CONSTANT   = "Não foi possivel criar o pedido."
        const val ERROR_PRODUCT_NOT_FOUND       = "Produto não encontrado."
        const val ERROR_ORDER_PRODUCT_NOT_FOUND = "Pedido ou Produto não encontrado."
        const val ERROR_USER_NOT_FOUND          = "Usuário não encontrado."
        const val ERROR_ORDER_NOT_FOUND         = "Pedido não encontrado."
        const val ERROR_ORDER_ERROR             = "Ocorreu um erro ao atualizar o pedido verifique os dados."
        const val ERROR_STATUS_NOT_FOUND        = "Status não encontrado utilize: PENDING, PREPARING, PAID, FINISHED"
        const val ERROR_CATEGORY_NOT_FOUD       = "Categoria do produto não encontrada."
        const val ERROR_ORDER_PAID_CONSTANT     = "Pedido já foi pago, caso queira adicionar novos produtos, cancele e inicie um novo pedido"
    }
}