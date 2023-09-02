package com.mvp.delivery.application.controller.v1.client


import com.mvp.delivery.domain.model.product.ProductRemoveOrderDTO
import com.mvp.delivery.domain.service.client.order.OrderService
import com.mvp.delivery.domain.model.order.*
import com.mvp.delivery.domain.model.order.store.QrDataDTO
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/order")
class OrderController(private val orderService: OrderService) {

    @PostMapping("/create-order")
    @Operation(
        summary = "Inicia um pedido",
        description = "Inicia um pedido informando e adiciona produdos pelo id",
        tags = ["Pedidos"]
    )
    fun createOrder(@RequestBody orderRequestDTO: OrderRequestDTO, authentication: Authentication): ResponseEntity<Mono<OrderResponseDTO>> {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO, authentication))
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca pedido id",
        description = "Busca pedido por id",
        tags = ["Pedidos"]
    )
    fun getOrderById(@PathVariable id: Long, authentication: Authentication): ResponseEntity<Mono<OrderByIdResponseDTO>> {
        return ResponseEntity.ok(orderService.getOrderById(id, authentication)
            .defaultIfEmpty(OrderByIdResponseDTO())
        )
    }

    @GetMapping("all-products-by-order-id/{id}")
    @Operation(
        summary = "Busca todos os produtos id do pedido",
        description = "Busca os produtos que estão associados por um pedido pelo id ",
        tags = ["Pedidos"]
    )
    fun getAllOrderProductsByIdOrder(@PathVariable id: Long, authentication: Authentication):  ResponseEntity<Flux<OrderProductResponseDTO>> {
        return  ResponseEntity.ok(
            orderService.getAllOrderProductsByIdOrder(id, authentication)
            .defaultIfEmpty(OrderProductResponseDTO())
        )
    }

    @PutMapping("/add-new-product-to-order")
    @Operation(
        summary = "Adiciona iten(s) ao pedido",
        description = "Adiciona iten(s) ao pedido recebe por uma lista de id(s) do(s) produto(s)",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateOrderProduct(@RequestBody orderRequestDTO: OrderRequestDTO, authentication: Authentication): ResponseEntity<Mono<OrderResponseDTO>> {
        return ResponseEntity.ok(orderService.updateOrderProduct(orderRequestDTO, authentication))
    }

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

    @PostMapping("/qr-code-checkout")
    @Operation(
        summary = "Efetua o pagamento atualizando os status",
        description = "Efetua o pagamento atualizando os status",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun checkoutOrder(authentication: Authentication): ResponseEntity<Mono<QrDataDTO>> {
        return ResponseEntity.ok(orderService.checkoutOrder(authentication))
    }

    @PutMapping("/webhook")
    @Operation(
        summary = "Recebe chamadas do Mercado Pago",
        description = "Efetua atualiza o status de pagamento",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun handlerWebHook(@RequestBody payload: String): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }
}