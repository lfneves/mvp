package com.mvp.delivery.application.controller.v1.admin


import com.mvp.delivery.domain.service.admin.order.OrderAdminService
import com.mvp.delivery.domain.model.order.OrderDTO
import com.mvp.delivery.domain.model.order.OrderFinishDTO
import com.mvp.delivery.domain.model.order.OrderStatusDTO
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
@RequestMapping("/api/v1/admin/order")
class OrderAdminController(private val orderAdminService: OrderAdminService) {

    @GetMapping
    @Operation(
        summary = "Busca todos pedidos",
        description = "Busca todos pedidos",
        tags = ["Admin Pedidos"]
    )
    fun all(): ResponseEntity<Flux<OrderDTO>> {
        return ResponseEntity.ok(orderAdminService.getOrders())
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @get:Operation(
        summary = "Perfomace Pedidos",
        description = "Utilizado para testes de performace e latência alterado o delay entre outros parametros",
        tags = ["Pedidos Performace"]
    )
    val flux: Flux<OrderDTO?>
        get() = orderAdminService.getOrders()
            .delayElements(Duration.ofSeconds(1)).log()

    @PutMapping("/update-order-status/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
        summary = "Atualiza o status do pedido",
        description = "Altera o status do pedido exemplo: PENDING, PREPARING, PAID, FINISHED ",
        tags = ["Admin Pedidos"]
    )
    fun updateOrderStatus(@PathVariable id: Int, @RequestBody orderStatusDTO: OrderStatusDTO, authentication: Authentication): ResponseEntity<Mono<OrderDTO>> {
        return ResponseEntity.ok(orderAdminService.updateOrderStatus(id, orderStatusDTO, authentication))
    }

    @PutMapping("/finish-order")
    @Operation(
        summary = "Finaliza o pedido atualizando os status",
        description = "Executa update dos status e fecha o pedido",
        tags = ["Admin Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateOrderFinishedAndStatus(@RequestBody orderFinishDTO: OrderFinishDTO, authentication: Authentication): ResponseEntity<Mono<OrderDTO>> {
        return ResponseEntity.ok(orderAdminService.updateOrderFinishedAndStatus(orderFinishDTO, authentication))
    }
}