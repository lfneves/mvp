package com.mvp.delivery.delivery.controller

import com.mvp.delivery.delivery.model.Order
import com.mvp.delivery.delivery.service.order.IOrderService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
    fun all(): Flux<Order> {
        return orderService.getOrders()
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    val flux: Flux<Order?>
        get() = orderService.getOrders()
            .delayElements(Duration.ofSeconds(1)).log()

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Int): Mono<Order> {
        return orderService.getOrderById(id)
            .defaultIfEmpty(Order())
    }

    @PutMapping("/update-order/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateOrder(@PathVariable id: Int, @RequestBody Order: Order): Mono<Order> {
        return orderService.updateOrder(id, Order)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrder(@PathVariable id: Int): Mono<Void> {
        return orderService.deleteOrderById(id)
    }
}