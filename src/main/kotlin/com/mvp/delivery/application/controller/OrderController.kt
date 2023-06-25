package com.mvp.delivery.application.controller


import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import com.mvp.delivery.domain.client.model.product.ProductRemoveOrderDTO
import com.mvp.delivery.domain.client.service.order.IOrderService
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
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO, authentication))
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Int): Mono<OrderDTO> {
        return orderService.getOrderById(id)
            .defaultIfEmpty(OrderDTO())
    }

    @GetMapping("all-products-by-order-id/{id}")
    fun getAllOrderProductsByIdOrder(@PathVariable id: Long): Flux<OrderProductDTO> {
        return orderService.getAllOrderProductsByIdOrder(id)
            .defaultIfEmpty(OrderProductDTO())
    }

    @PutMapping("/update-order-product/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun updateOrderProduct(@PathVariable id: Int, @RequestBody orderDTO: OrderDTO): OrderDTO {
        return orderService.updateOrderProduct(id, orderDTO)
    }

    @PutMapping("/update-order-status/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun updateOrderStatus(@PathVariable id: Int, @RequestBody orderStatusDTO: OrderStatusDTO): ResponseEntity<Mono<OrderDTO>> {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, orderStatusDTO))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun deleteOrder(@PathVariable id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(orderService.deleteOrderById(id))
    }

    @DeleteMapping("/remove-product-order")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun deleteOrderProductById(@RequestBody productRemoveOrderDTO: ProductRemoveOrderDTO): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(orderService.deleteOrderProductById(productRemoveOrderDTO))
    }
}