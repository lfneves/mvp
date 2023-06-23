package com.mvp.delivery.application.controller


import com.mvp.delivery.domain.client.model.order.OrderDTO
import com.mvp.delivery.domain.client.model.order.OrderRequestDTO
import com.mvp.delivery.domain.client.model.order.OrderResponseDTO
import com.mvp.delivery.domain.client.service.order.IOrderService
import com.mvp.delivery.domain.exception.Exceptions
import kotlinx.coroutines.reactive.awaitFirstOrElse
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
    suspend fun createOrder(@RequestBody orderRequestDTO: OrderRequestDTO, authentication: Authentication): ResponseEntity<Flux<OrderResponseDTO>> {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO, authentication))
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Int): Mono<OrderDTO> {
        return orderService.getOrderById(id)
            .defaultIfEmpty(OrderDTO())
    }

    @PutMapping("/update-order/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun updateOrder(@PathVariable id: Int, @RequestBody orderDTO: OrderDTO): OrderDTO {
        return orderService.updateOrder(id, orderDTO)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrder(@PathVariable id: Int): Mono<Void> {
        return orderService.deleteOrderById(id)
    }
}