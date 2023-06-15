package com.mvp.delivery.delivery

import com.mvp.delivery.delivery.model.Address
import com.mvp.delivery.delivery.model.Category
import com.mvp.delivery.delivery.model.Product
import com.mvp.delivery.delivery.model.User
import com.mvp.delivery.delivery.repository.item.ICategoryRepository
import com.mvp.delivery.delivery.repository.user.IAddressRepository
import com.mvp.delivery.delivery.service.item.IProductService
import com.mvp.delivery.delivery.service.user.IUserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import reactor.core.publisher.Mono
import java.math.BigDecimal

@SpringBootApplication
@EnableR2dbcAuditing
class ReactiveRestApiDemoApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ReactiveRestApiDemoApplication::class.java, *args)
        }
    }

    @Bean
    fun loadData(
        userService: IUserService,
        productService: IProductService,
        categoryRepository: ICategoryRepository,
        addressRepository: IAddressRepository
    ): CommandLineRunner {
        return CommandLineRunner {
            // save a initial user
            userService.deleteAllUsers().subscribe()
            addressRepository.deleteAll().subscribe()

            Mono.just(
                Address(null, "Rua admin", "São Paulo", "SP", "12345123")
            ).flatMap {
                addressRepository.save(it)
                    .flatMap {address ->
                        Mono.just(
                            User(null, "admin", "admin@email.com", "12345678912", "admin", address.id)
                        ).flatMap { user -> userService.saveUser(user) }
                    }
            }.subscribe()
//            userService.saveInitialUser(users)

            productService.deleteAllProducts().subscribe()
            categoryRepository.deleteAll().subscribe()

            Mono.just(
                Category(
                    name = "Bebidas"
                )
            ).flatMap {
                categoryRepository.save(it)
                    .flatMap { category ->
                        Mono.just(
                            Product(
                                name = "Suco",
                                price = BigDecimal.ONE,
                                quantity = 1,
                                idCategory = category.id
                            )
                        ).flatMap { product -> productService.saveProduct(product) }
                    }
            }.subscribe()

            categoryRepository.findByName("Bebidas")
                .flatMap { category ->
                    Mono.just(
                        Product(
                            name = "Refrigerante",
                            price = BigDecimal.ONE,
                            quantity = 1,
                            idCategory = category.id
                        )
                    ).flatMap { product -> productService.saveProduct(product) }
                }.subscribe()

            Mono.just(
                Category(
                    name = "Lanche",
                )
            ).flatMap {
                categoryRepository.save(it)
                    .flatMap { category ->
                        Mono.just(
                            Product(
                                name = "Hamburguer",
                                price = BigDecimal.TEN,
                                quantity = 1,
                                idCategory = category.id
                            )
                        ).flatMap { productService.saveProduct(it) }
                    }
            }.subscribe()

            Mono.just(
                Category(
                    name = "Sobremesa",
                )
            ).flatMap {
                categoryRepository.save(it)
                    .flatMap { category ->
                        Mono.just(
                            Product(
                                name = "Pudim",
                                price = BigDecimal.ONE,
                                quantity = 1,
                                idCategory = category.id
                            )
                        ).flatMap { productService.saveProduct(it) }
                    }
            }.subscribe()

            Mono.just(
                Category(
                    name = "Acompanhamento",
                )
            ).flatMap {
                categoryRepository.save(it)
                    .flatMap { category ->
                        Mono.just(
                            Product(
                                name = "Batata",
                                price = BigDecimal.ONE,
                                quantity = 1,
                                idCategory = category.id
                            )
                        ).flatMap { productService.saveProduct(it) }
                    }
            }.subscribe()
        }
    }
}