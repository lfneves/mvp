package com.mvp.delivery.application.controller


import com.mvp.delivery.domain.client.service.order.IOrderService
import com.mvp.delivery.infrastruture.entity.order.OrderEntity
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
    fun all(): Flux<OrderEntity> {
        return orderService.getOrders()
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    val flux: Flux<OrderEntity?>
        get() = orderService.getOrders()
            .delayElements(Duration.ofSeconds(1)).log()

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Int): Mono<OrderEntity> {
        return orderService.getOrderById(id)
            .defaultIfEmpty(OrderEntity())
    }

    @PutMapping("/update-order/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateOrder(@PathVariable id: Int, @RequestBody orderEntity: OrderEntity): Mono<OrderEntity> {
        return orderService.updateOrder(id, orderEntity)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrder(@PathVariable id: Int): Mono<Void> {
        return orderService.deleteOrderById(id)
    }
}