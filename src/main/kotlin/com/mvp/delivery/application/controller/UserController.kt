package com.mvp.delivery.application.controller

import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.domain.client.service.user.IUserService
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.user.UserEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/api/v1/users")
class UserController(IUserService: IUserService) {
    var logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    private val userService: IUserService

    init {
        this.userService = IUserService
    }

    @GetMapping
    fun all(): Flux<UserDTO> {
        return userService.getUsers()
    }

    @get:GetMapping(path = ["/all-users"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    val flux: Flux<UserDTO?>
        get() = userService.getUsers()
            .delayElements(Duration.ofSeconds(1)).log()

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody user: UserDTO): Mono<UserDTO> {
        return userService.signup(user)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Could not create user.")))
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): Mono<UserDTO> {
        return userService.getUserById(id)
            .defaultIfEmpty(UserDTO())
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody userEntity: UserEntity): Mono<UserEntity> {
        return userService.saveUser(userEntity)
    }

    @PutMapping("/update-user/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateUser(@PathVariable id: Int, @RequestBody user: UserDTO): Mono<UserDTO> {
        return userService.updateUser(id, user)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Int): Mono<Void> {
        return userService.deleteUser(id)
    }
}