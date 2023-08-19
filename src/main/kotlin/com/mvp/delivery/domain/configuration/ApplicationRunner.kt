package com.mvp.delivery.domain.configuration

import com.mvp.delivery.domain.service.admin.product.ProductAdminService
import com.mvp.delivery.domain.service.admin.user.UserAdminService
import com.mvp.delivery.domain.service.client.user.UserService
import com.mvp.delivery.infrastruture.entity.product.CategoryEntity
import com.mvp.delivery.infrastruture.entity.product.ProductEntity
import com.mvp.delivery.infrastruture.entity.user.AddressEntity
import com.mvp.delivery.infrastruture.entity.user.UserEntity
import com.mvp.delivery.infrastruture.repository.order.OrderRepository
import com.mvp.delivery.infrastruture.repository.order.OrderProductRepository
import com.mvp.delivery.infrastruture.repository.product.CategoryRepository
import com.mvp.delivery.infrastruture.repository.user.AddressRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import reactor.core.publisher.Mono
import java.math.BigDecimal

// Start class for test create user, product and category on start application
/***
 * Class created for study purposes and currently disabled, using docker compose and k8s create/insert database
 */
class ApplicationRunner: CommandLineRunner {

    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var userAdminService: UserAdminService
    @Autowired private lateinit var productAdminService: ProductAdminService
    @Autowired private lateinit var categoryRepository: CategoryRepository
    @Autowired private lateinit var addressRepository: AddressRepository
    @Autowired private lateinit var orderRepository: OrderRepository
    @Autowired private lateinit var orderProductRepository: OrderProductRepository

    override fun run(vararg args: String?) {
            orderProductRepository.deleteAll().subscribe()
            orderRepository.deleteAll().subscribe()
            productAdminService.deleteAllProducts().subscribe()
            categoryRepository.deleteAll().subscribe()
            userAdminService.deleteAllUsers().subscribe()
            addressRepository.deleteAll().subscribe()


            // save a initial user
            Mono.just(
                AddressEntity(null, "Rua admin", "São Paulo", "SP", "12345123")
            ).flatMap {
                addressRepository.save(it)
                    .flatMap {address ->
                        Mono.just(
                            UserEntity(null, "admin", "admin@email.com", "99999999999", "admin", address.id)
                        ).flatMap { user -> userService.saveUser(user.toDTO()) }
                    }
            }.subscribe()

            // save a initial products and category's
            Mono.just(
                CategoryEntity(
                    name = "Bebidas",
                    description = "Consumível líquido que possa ser comprado para consumo, como Água, Refrigerante, Cerveja entre outros."
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
                        ).flatMap { product -> productAdminService.saveProduct(product.toDTO(category.toDTO())) }
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
                    ).flatMap { product -> productAdminService.saveProduct(product.toDTO(category.toDTO())) }
                }.subscribe()

            Mono.just(
                CategoryEntity(
                    name = "Lanche",
                    description = "Refeição pronta para consumo que possa ser comprada como Hambuguer, Pizza, Sanduiche entre outros."
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
                        ).flatMap { productAdminService.saveProduct(it.toDTO(category.toDTO())) }
                    }
            }.subscribe()

            Mono.just(
                CategoryEntity(
                    name = "Sobremesa",
                    description = "Algum doce, pode incluir bolos, tortas, sorvetes, pudins, entre outros."
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
                        ).flatMap { productAdminService.saveProduct(it.toDTO(category.toDTO())) }
                    }
            }.subscribe()

            Mono.just(
                CategoryEntity(
                    name = "Acompanhamento",
                    description = "Todo e qualquer alimento de pequena média proporção que possa ser consumido com outras refeições."
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
                        ).flatMap { productAdminService.saveProduct(it.toDTO(category.toDTO())) }
                    }
            }.subscribe()
        }
    }