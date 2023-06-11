package com.mvp.delivery.delivery

import com.mvp.delivery.delivery.model.Address
import com.mvp.delivery.delivery.model.User
import com.mvp.delivery.delivery.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Flux
import java.util.concurrent.ThreadLocalRandom

@SpringBootApplication
class ReactiveRestApiDemoApplication {

    @Bean
    fun loadData(userService: UserService): CommandLineRunner {
        return CommandLineRunner {
            // save a couple of users
            userService.deleteAllUsers()
            val users: Flux<User> = Flux.just(
                User(
                    "Lucas","123", 10, 1,  Address("sp")
                )
            )
            userService.saveUser(users)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ReactiveRestApiDemoApplication::class.java, *args)
        }
    }
}