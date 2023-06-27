package com.mvp.delivery.application.controller


import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import com.mvp.delivery.domain.client.model.product.ProductRemoveOrderDTO
import com.mvp.delivery.domain.client.service.order.IOrderService
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.utils.constants.ErrorMsgConstants
import io.swagger.v3.oas.annotations.Operation
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
    @Operation(
        summary = "Busca todos pedidos",
        description = "Busca todos pedidos",
        tags = ["Admin-Pedidos"]
    )
    fun all(): Flux<OrderDTO> {
        return orderService.getOrders()
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @get:Operation(
        summary = "Perfomace Pedidos",
        description = "Utilizado para testes de performace e latência alterado o delay entre outros parametros",
        tags = ["Pedidos Performace"]
    )
    val flux: Flux<OrderDTO?>
        get() = orderService.getOrders()
            .delayElements(Duration.ofSeconds(1)).log()

    @PostMapping("/create-order")
    @Operation(
        summary = "Inicia um pedido",
        description = "Inicia um pedido informando e adiciona produdos pelo id",
        tags = ["Pedidos"]
    )
    suspend fun createOrder(@RequestBody orderRequestDTO: OrderRequestDTO, authentication: Authentication): ResponseEntity<Mono<OrderResponseDTO>> {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO, authentication))
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca pedido id",
        description = "Busca pedido por id",
        tags = ["Pedidos"]
    )
    fun getOrderById(@PathVariable id: Int, authentication: Authentication): Mono<OrderDTO> {
        return orderService.getOrderById(id, authentication)
            .defaultIfEmpty(OrderDTO())
    }

    @GetMapping("all-products-by-order-id/{id}")
    @Operation(
        summary = "Busca todos os produtos id do pedido",
        description = "Busca os produtos que estão associados por um pedido pelo id ",
        tags = ["Pedidos"]
    )
    fun getAllOrderProductsByIdOrder(@PathVariable id: Long, authentication: Authentication): Flux<OrderProductDTO> {
        return orderService.getAllOrderProductsByIdOrder(id, authentication)
            .defaultIfEmpty(OrderProductDTO())
    }

//    @PutMapping("/update-order-product/{id}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    suspend fun updateOrderProduct(@PathVariable id: Int, @RequestBody orderDTO: OrderDTO, authentication: Authentication): OrderDTO {
//        return orderService.updateOrderProduct(id, orderDTO, authentication)
//    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remove pedido pelo id",
        description = "Remove um pedido e todos produtos pelo id",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun deleteOrder(@PathVariable id: Int, authentication: Authentication): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(orderService.deleteOrderById(id, authentication))
    }

    @DeleteMapping("/remove-product-order")
    @Operation(
        summary = "Remove produto de um pedido",
        description = "Remove um produto do pedido informado pelo id",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun deleteOrderProductById(@RequestBody productRemoveOrderDTO: ProductRemoveOrderDTO, authentication: Authentication): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(orderService.deleteOrderProductById(productRemoveOrderDTO, authentication))
    }

    @PutMapping("/update-order-status/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
        summary = "Atualiza o status do pedido",
        description = "Altera o status do pedido exemplo: PENDING, PREPARING, PAID, FINISHED ",
        tags = ["Admin-Pedidos"]
    )
    suspend fun updateOrderStatus(@PathVariable id: Int, @RequestBody orderStatusDTO: OrderStatusDTO, authentication: Authentication): ResponseEntity<Mono<OrderDTO>> {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, orderStatusDTO, authentication))
    }

    @PutMapping("/finish-order")
    @Operation(
        summary = "Finaliza o pedido atualizando os status",
        description = "Executa update dos status e fecha o pedido",
        tags = ["Admin-Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun updateOrderFinishedAndStatus(@RequestBody orderFinishDTO: OrderFinishDTO, authentication: Authentication): ResponseEntity<Mono<OrderDTO>> {
        return ResponseEntity.ok(orderService.updateOrderFinishedAndStatus(orderFinishDTO, authentication))
    }
}