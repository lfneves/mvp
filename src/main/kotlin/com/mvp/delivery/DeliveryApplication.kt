package com.mvp.delivery

import com.mvp.delivery.domain.client.service.product.IProductService
import com.mvp.delivery.domain.client.service.user.IUserService
import com.mvp.delivery.infrastruture.entity.product.CategoryEntity
import com.mvp.delivery.infrastruture.entity.product.ProductEntity
import com.mvp.delivery.infrastruture.entity.user.AddressDTO
import com.mvp.delivery.infrastruture.entity.user.UserEntity
import com.mvp.delivery.infrastruture.repository.product.ICategoryRepository
import com.mvp.delivery.infrastruture.repository.user.IAddressRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import reactor.core.publisher.Mono
import java.math.BigDecimal

@SpringBootApplication
@EnableR2dbcAuditing
class DeliveryApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(com.mvp.delivery.DeliveryApplication::class.java, *args)
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

            userService.deleteAllUsers().subscribe()
            addressRepository.deleteAll().subscribe()
            productService.deleteAllProducts().subscribe()
            categoryRepository.deleteAll().subscribe()

            // save a initial user
            Mono.just(
                AddressDTO(null, "Rua admin", "São Paulo", "SP", "12345123")
            ).flatMap {
                addressRepository.save(it)
                    .flatMap {address ->
                        Mono.just(
                            UserEntity(null, "admin", "admin@email.com", "12345678912", "123", address.id)
                        ).flatMap { user -> userService.saveUser(user) }
                    }
            }.subscribe()

            // save a initial products and category's
            Mono.just(
                CategoryEntity(
                    name = "Bebidas"
                )
            ).flatMap {
                categoryRepository.save(it)
                    .flatMap { category ->
                        Mono.just(
                            ProductEntity(
                                name = "Suco",
                                price = BigDecimal.ONE,
                                quantity = 1,
                                idCategory = category.id
                            )
                        ).flatMap { product -> productService.saveProduct(product.toDTO(category.toDTO())) }
                    }
            }.subscribe()

            categoryRepository.findByName("Bebidas")
                .flatMap { category ->
                    Mono.just(
                        ProductEntity(
                            name = "Refrigerante",
                            price = BigDecimal.ONE,
                            quantity = 1,
                            idCategory = category.id
                        )
                    ).flatMap { product -> productService.saveProduct(product.toDTO(category.toDTO())) }
                }.subscribe()

            Mono.just(
                CategoryEntity(
                    name = "Lanche",
                )
            ).flatMap { categoryEntity ->
                categoryRepository.save(categoryEntity)
                    .flatMap { category ->
                        Mono.just(
                            ProductEntity(
                                name = "Hamburguer",
                                price = BigDecimal.TEN,
                                quantity = 1,
                                idCategory = category.id
                            )
                        ).flatMap { productService.saveProduct(it.toDTO(category.toDTO())) }
                    }
            }.subscribe()

            Mono.just(
                CategoryEntity(
                    name = "Sobremesa",
                )
            ).flatMap { categoryEntity ->
                categoryRepository.save(categoryEntity)
                    .flatMap { category ->
                        Mono.just(
                            ProductEntity(
                                name = "Pudim",
                                price = BigDecimal.ONE,
                                quantity = 1,
                                idCategory = category.id
                            )
                        ).flatMap { productService.saveProduct(it.toDTO(category.toDTO())) }
                    }
            }.subscribe()

            Mono.just(
                CategoryEntity(
                    name = "Acompanhamento",
                )
            ).flatMap { categoryEntity ->
                categoryRepository.save(categoryEntity)
                    .flatMap { category ->
                        Mono.just(
                            ProductEntity(
                                name = "Batata",
                                price = BigDecimal.ONE,
                                quantity = 1,
                                idCategory = category.id
                            )
                        ).flatMap { productService.saveProduct(it.toDTO(category.toDTO())) }
                    }
            }.subscribe()
        }
    }
}