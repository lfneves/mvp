package com.mvp.delivery.application.controller


import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import com.mvp.delivery.domain.client.model.product.ProductRemoveOrderDTO
import com.mvp.delivery.domain.client.service.order.IOrderService
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.utils.constants.ErrorMsgConstants
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/api/v1/order")
class OrderController(orderService: IOrderService) {
    private val orderService: IOrderService

    init {
        this.orderService = orderService
    }

    @GetMapping
    fun all(): Flux<OrderDTO> {
        return orderService.getOrders()
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    val flux: Flux<OrderDTO?>
        get() = orderService.getOrders()
            .delayElements(Duration.ofSeconds(1)).log()

    @PostMapping("/create-order")
    suspend fun createOrder(@RequestBody orderRequestDTO: OrderRequestDTO, authentication: Authentication): ResponseEntity<Mono<OrderResponseDTO>> {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO, authentication)
            .onErrorMap {
                Exceptions.NotFoundException(ErrorMsgConstants.ERROR_CREATE_ORDER_CONSTANT)
        })
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Int, authentication: Authentication): Mono<OrderDTO> {
        return orderService.getOrderById(id, authentication)
            .defaultIfEmpty(OrderDTO())
    }

    @GetMapping("all-products-by-order-id/{id}")
    fun getAllOrderProductsByIdOrder(@PathVariable id: Long, authentication: Authentication): Flux<OrderProductDTO> {
        return orderService.getAllOrderProductsByIdOrder(id, authentication)
            .defaultIfEmpty(OrderProductDTO())
    }

    @PutMapping("/update-order-product/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun updateOrderProduct(@PathVariable id: Int, @RequestBody orderDTO: OrderDTO, authentication: Authentication): OrderDTO {
        return orderService.updateOrderProduct(id, orderDTO, authentication)
    }

    @PutMapping("/update-order-status/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun updateOrderStatus(@PathVariable id: Int, @RequestBody orderStatusDTO: OrderStatusDTO, authentication: Authentication): ResponseEntity<Mono<OrderDTO>> {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, orderStatusDTO, authentication))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun deleteOrder(@PathVariable id: Int, authentication: Authentication): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(orderService.deleteOrderById(id, authentication))
    }

    @DeleteMapping("/remove-product-order")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun deleteOrderProductById(@RequestBody productRemoveOrderDTO: ProductRemoveOrderDTO, authentication: Authentication): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(orderService.deleteOrderProductById(productRemoveOrderDTO, authentication))
    }
}