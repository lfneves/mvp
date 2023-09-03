package com.mvp.delivery.domain.service.client.order

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mvp.delivery.domain.configuration.OrderEndpointPropertyConfiguration
import com.mvp.delivery.domain.configuration.OrderPropertyConfiguration
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.domain.model.auth.AuthenticationVO
import com.mvp.delivery.domain.model.order.OrderByIdResponseDTO
import com.mvp.delivery.domain.model.order.enums.OrderStatusEnum
import com.mvp.delivery.domain.model.order.store.*
import com.mvp.delivery.domain.model.order.store.webhook.MerchantOrderDTO
import com.mvp.delivery.domain.model.order.store.webhook.MerchantOrderResponseDTO
import com.mvp.delivery.infrastruture.repository.order.OrderRepository
import com.mvp.delivery.utils.constants.ErrorMsgConstants
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.Duration
import java.util.*
import java.util.function.Function
import java.util.function.Predicate

@Service
class MPOrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderService: OrderService,
    private val orderPropertyConfiguration: OrderPropertyConfiguration,
    private val orderEndpointPropertyConfiguration: OrderEndpointPropertyConfiguration
): MPOrderService {

    private val client = WebClient.create()

    override fun checkoutOrder(authentication: Authentication): Mono<QrDataDTO> {
        authentication as AuthenticationVO
        return orderRepository.findByUsername(authentication.iAuthDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .flatMap {
                orderService.getOrderById(it.id!!, authentication)
                    .flatMap {
                        Mono.just(orderCheckoutGenerateQrs(it))
                    }.map { jsonRequest ->
                        jsonRequest
                    }
            }.flatMap {
                generateOrderQrs(it)
            }
    }

    override suspend fun saveCheckoutOrderExternalStoreID(merchantOrderDTO: MerchantOrderDTO): Mono<Void> {
        val  test = getMerchantOrderByID(merchantOrderDTO.resource).awaitSingle()
        val order = orderService.getOrderByExternalId(UUID.fromString(test.externalReference))
        order?.status = if (test.orderStatus == "payment_required") OrderStatusEnum.PAYMENT_REQUIRED.value
            else OrderStatusEnum.PENDING.value
        orderRepository.updateStatus(order!!.toEntity())
        return Mono.empty()
    }

    override fun getMerchantOrderByID(requestUrl: String): Mono<MerchantOrderResponseDTO> {
//        val client = WebClient.create()
        return client.get()
            .uri(requestUrl)
            .header("Authorization", orderPropertyConfiguration.token)
            .retrieve()
            .bodyToMono(MerchantOrderResponseDTO::class.java)
    }

    override fun generateOrderQrs(requestBody: String): Mono<QrDataDTO> {

        val endpoint = orderEndpointPropertyConfiguration.qrs.replace("?", orderPropertyConfiguration.userId)
        val requestUrl = orderPropertyConfiguration.url + endpoint
        val responseSpec = client.put()
            .uri(requestUrl)
            .header("Authorization", orderPropertyConfiguration.token)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(QrDataDTO::class.java)

        return responseSpec
    }

    override fun orderCheckoutGenerateQrs(order: OrderByIdResponseDTO): String {
        val mapper = jacksonObjectMapper()
        val products = mutableListOf<ItemDTO>()
        val orderQrsDTO = OrderQrsDTO()

        orderQrsDTO.externalReference = order.externalId.toString()
        orderQrsDTO.title = "${order.id}"
        orderQrsDTO.notificationUrl = orderPropertyConfiguration.notificationUrl
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