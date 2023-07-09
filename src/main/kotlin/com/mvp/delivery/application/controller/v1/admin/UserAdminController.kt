package com.mvp.delivery.application.controller.v1.admin

import com.mvp.delivery.domain.model.user.UserDTO
import com.mvp.delivery.domain.service.admin.user.UserAdminService
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/api/v1/admin/users")
class UserAdminController(private val userAdminService: UserAdminService) {
    var logger: Logger = LoggerFactory.getLogger(UserAdminController::class.java)

    @GetMapping
    @Operation(
        summary = "Busca todos usuários",
        description = "Busca todos usuários cadastrados não valida usuárioa admin neste momento",
        tags = ["Administrador de Usuários"]
    )
    fun all(): ResponseEntity<Flux<UserDTO>> {
        return ResponseEntity.ok(userAdminService.getUsers())
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @get:Operation(
        summary = "Perfomace Usuários",
        description = "Utilizado para testes de performace e latência alterado o delay entre outros parametros",
        tags = ["Performace Usuários"]
    )
    val flux: ResponseEntity<Flux<UserDTO>>
        get() = ResponseEntity.ok(userAdminService.getUsers()
            .delayElements(Duration.ofSeconds(1)).log())

    @DeleteMapping("/delete-all")
    @Operation(
        summary = "Deleta todos usuários",
        description = "Deleta todos usuários não valida usuárioa admin neste momento - only for development uses",
        tags = ["Administrador de Usuários"]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAllUsers(authentication: Authentication): ResponseEntity<Mono<Void>> {
        logger.info("Admin - delete all users")
        return ResponseEntity.ok(userAdminService.deleteAllUsers())
    }
}