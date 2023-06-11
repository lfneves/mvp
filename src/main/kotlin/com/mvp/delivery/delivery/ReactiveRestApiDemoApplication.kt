package com.mvp.delivery.delivery

import com.mvp.delivery.delivery.model.User
import com.mvp.delivery.delivery.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Flux

@SpringBootApplication
class ReactiveRestApiDemoApplication {

    @Bean
    fun loadData(userService: UserService): CommandLineRunner {
        return CommandLineRunner {
            // save a couple of users
//            userService.deleteAllUsers()
            val users =
                User(
                    "Lucas","123"
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