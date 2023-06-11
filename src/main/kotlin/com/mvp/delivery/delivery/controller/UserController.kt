package com.mvp.delivery.delivery.controller

import com.mvp.delivery.delivery.model.User
import com.mvp.delivery.delivery.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/api/v1/users")
class UserController(userService: UserService) {
    private val userService: UserService

    init {
        this.userService = userService
    }

    @GetMapping
    fun all(): Flux<User> {
        return userService.getUsers()
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    val flux: Flux<User?>
        get() = userService.getUsers()
            .delayElements(Duration.ofSeconds(1)).log()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): Mono<User> {
        return userService.getUserById(id)
            .defaultIfEmpty(User())
    }

    @GetMapping("/guests/{id}")
    fun getGuestUser(@PathVariable id: Int): Mono<User> {
        return userService.getGuestUserById(id)
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody user: User): Mono<User> {
        return userService.saveUser(user)
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody user: User): Mono<User> {
        return userService.saveUser(user)
    }

    @PutMapping("/update-user/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateUser(@PathVariable id: Int, @RequestBody user: User): Mono<User> {
        return userService.updateUser(id, user)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Int): Mono<Void> {
        return userService.deleteUser(id)
    }
}