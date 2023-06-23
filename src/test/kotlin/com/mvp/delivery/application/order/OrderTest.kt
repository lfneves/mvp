package com.mvp.delivery.application.order

import com.mvp.delivery.domain.client.model.auth.AuthClientDTO
import com.mvp.delivery.domain.client.model.order.OrderDTO
import com.mvp.delivery.domain.client.service.order.OrderServiceImpl
import com.mvp.delivery.domain.client.service.user.UserServiceImpl
import com.mvp.delivery.helpers.OrderMock
import com.mvp.delivery.helpers.UserMock
import com.mvp.delivery.infrastruture.repository.order.OrderProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.assertj.core.api.Assertions.assertThat
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest
class OrderTest {

    @Autowired private lateinit var orderService: OrderServiceImpl
    @Autowired private lateinit var orderProductRepository: OrderProductRepository
    @Autowired private lateinit var userServiceImpl: UserServiceImpl
    private lateinit var authentication: Authentication

    @BeforeEach
    fun init() {
        authentication = AuthClientDTO(
            0,
            "12345678912",
            password = "123"
        ).toAuthentication()
        ReactiveSecurityContextHolder.withAuthentication(authentication)
    }

    @Test
    fun test1() {
        var orderRequestDTO = OrderMock.mockOrderRequest()
        val user = userServiceImpl.signup(UserMock.mockUserRegistrationRequest())
            .doOnNext {
                it.cpf?.let { it1 -> orderRequestDTO.copy(username = it1) }
            }.map { it }

//        val response = orderService.createOrder(orderRequestDTO)

//        println(response)
//        val response: Flux<OrderProductEntity> = orderProductRepository.findAllByIdOrder(newOrder.orderDTO?.id!!)
//        println(response.map { it })
    }
}