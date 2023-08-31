package com.mvp.delivery.domain.model.order.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mvp.delivery.domain.model.order.OrderByIdResponseDTO
import reactor.core.publisher.Mono




data class OrderQrsDTO(
    @JsonProperty("cash_out")
    var cashOut: CashOutDTO = CashOutDTO(),
    var description: String = "",
    @JsonProperty("external_reference")
    var externalReference: String = "",
    @JsonProperty("items")
    var itemDTOS: List<ItemDTO> = listOf(),
    @JsonProperty("notification_url")
    var notificationUrl: String? = "",
    @JsonProperty("sponsor")
    var sponsorDTO: SponsorDTO = SponsorDTO(),
    var title: String = "",
    @JsonProperty("total_amount")
    var totalAmount: Int = 0
) {
    fun orderCheckoutGerateQrs(order: OrderByIdResponseDTO): String {
        val mapper = jacksonObjectMapper()
        val items = mutableListOf<ItemDTO>()
        val orderQrsDTO = OrderQrsDTO()
        orderQrsDTO.externalReference = "123"
        orderQrsDTO.title = "title"
        orderQrsDTO.notificationUrl = "https://webhook.site/3a01d6b2-7c0c-4c45-a5c7-5b305a11a3a8"
        orderQrsDTO.totalAmount = 2
        orderQrsDTO.description = "description"

        val item = ItemDTO(
            category = "Test",
            description = "Test",
            quantity = 1,
            sku_number = "TesteSku",
            title = "Test Item",
            total_amount = 1,
            unit_measure = "unit",
            unit_price = 1
        )
        val sponsor = SponsorDTO(id = 57174696)
        val cashOut = CashOutDTO(amount = 1)
        orderQrsDTO.itemDTOS = listOf(item)
        orderQrsDTO.sponsorDTO = sponsor
        orderQrsDTO.cashOut = cashOut

        return mapper.writeValueAsString(orderQrsDTO)
    }
}