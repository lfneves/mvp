package com.mvp.delivery.application.controller

import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.domain.client.model.user.UsernameDTO
import com.mvp.delivery.domain.client.service.user.UserService
import com.mvp.delivery.domain.exception.Exceptions
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
@RequestMapping("/api/v1/users")
class UserController(userService: UserService) {
    var logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    private val userService: UserService

    init {
        this.userService = userService
    }

    @GetMapping
    @Operation(
        summary = "Busca todos usuários",
        description = "Busca todos usuários cadastrados",
        tags = ["Administrador de Usuários"]
    )
    fun all(): ResponseEntity<Flux<UserDTO>> {
        return ResponseEntity.ok(userService.getUsers())
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @get:Operation(
        summary = "Perfomace Usuários",
        description = "Utilizado para testes de performace e latência alterado o delay entre outros parametros",
        tags = ["Performace Usuários"]
    )
    val flux: ResponseEntity<Flux<UserDTO>>
        get() = ResponseEntity.ok(userService.getUsers()
            .delayElements(Duration.ofSeconds(1)).log())

    @PostMapping("/signup")
    @Operation(
        summary = "Cadastro de Usuário",
        description = "Cadastro usuário usado quando não possui usuário e senha",
        tags = ["Usuários"]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody user: UserDTO): ResponseEntity<Mono<UserDTO>> {
        logger.info("/signup")
        return ResponseEntity.ok(userService.signup(user)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Could not create user."))))
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca Usuário por id",
        description = "Busca usuário usando o id informado",
        tags = ["Usuários"]
    )
    fun getUserById(@PathVariable id: Int, authentication: Authentication): ResponseEntity<Mono<UserDTO>> {
        logger.info("/getUserById")
        return ResponseEntity.ok(userService.getUserById(id, authentication)
            .defaultIfEmpty(UserDTO()))
    }

    @GetMapping("/get-by-username")
    @Operation(
        summary = "Busca Usuário por username(CPF)",
        description = "Busca Usuário por username(CPF) cadastrado",
        tags = ["Usuários"]
    )
    fun getUserByUsername(@RequestBody usernameDTO: UsernameDTO, authentication: Authentication): ResponseEntity<Mono<UserDTO>> {
        logger.info("/getbyUsername")
        return ResponseEntity.ok(userService.getByUsername(usernameDTO, authentication)
            .defaultIfEmpty(UserDTO()))
    }


    @PostMapping("/create")
    @Operation(
        hidden = true,
        summary = "(Não utilizado use o /signup) Criação Usuário",
        description = "Chamada não utilizada nesse momento após o signup",
        tags = ["Usuários (Deprecated)"]
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Deprecated("Not used - use signup function",
        level = DeprecationLevel.WARNING,
        replaceWith = ReplaceWith("/signup")
    )
    fun createUser(@RequestBody user: UserDTO): ResponseEntity<Mono<UserDTO>> {
        logger.info("/create")
        return ResponseEntity.ok(userService.saveUser(user))
    }

    @PutMapping("/update-user/{id}")
    @Operation(
        summary = "Atualiza Usuário por id",
        description = "Atualiza Usuário por id e dados informados",
        tags = ["Usuários"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateUser(@PathVariable id: Int, @RequestBody user: UserDTO, authentication: Authentication): ResponseEntity<Mono<UserDTO>> {
        logger.info("/update-user/{id}")
        return ResponseEntity.ok(userService.updateUser(id, user, authentication))
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deleta Usuário por id",
        description = "Deleta Usuário por id",
        tags = ["Usuários"]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Int, authentication: Authentication): ResponseEntity<Mono<Void>> {
        logger.info("deleteUserById")
        return ResponseEntity.ok(userService.deleteUserById(id, authentication))
    }
}