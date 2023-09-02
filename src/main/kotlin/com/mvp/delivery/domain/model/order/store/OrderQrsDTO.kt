package com.mvp.delivery.domain.model.order.store

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mvp.delivery.domain.model.order.OrderByIdResponseDTO

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
    fun orderCheckoutGenerateQrs(order: OrderByIdResponseDTO): String {
        val mapper = jacksonObjectMapper()
        val products = mutableListOf<ItemDTO>()
        val orderQrsDTO = OrderQrsDTO()

        orderQrsDTO.externalReference = "${order.idClient}_${order.id}"
        orderQrsDTO.title = "${order.id}"
        orderQrsDTO.notificationUrl = "https://webhook.site/3a01d6b2-7c0c-4c45-a5c7-5b305a11a3a8"
        orderQrsDTO.description = order.status

        order.products.forEach {
            val product = ItemDTO(
                category = it.categoryName!!,
                description = "Test",
                quantity = 1,
                sku_number = "${order.idClient}_${order.id}",
                title = it.productName!!,
                total_amount = 1,
                unit_measure = "unit",
                unit_price = it.price.toInt()
            )
            products.add(product)
        }
        val sponsor = SponsorDTO(id = 57174696)
        orderQrsDTO.totalAmount = products.sumOf { it.total_amount + it.unit_price }
        val cashOut = CashOutDTO(amount = products.sumOf { it.unit_price })
        orderQrsDTO.itemDTOS = products
        orderQrsDTO.sponsorDTO = sponsor
        orderQrsDTO.cashOut = cashOut

        return mapper.writeValueAsString(orderQrsDTO)
    }
}