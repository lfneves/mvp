package com.mvp.delivery.application.order

import com.mvp.delivery.domain.client.model.auth.AuthClientDTO
import com.mvp.delivery.domain.client.service.order.OrderServiceImpl
import com.mvp.delivery.domain.client.service.user.UserServiceImpl
import com.mvp.delivery.domain.configuration.ApplicationRunner
import com.mvp.delivery.helpers.OrderMock
import com.mvp.delivery.infrastruture.repository.order.OrderProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder


@SpringBootTest
class OrderTest {

    @SpyBean
    private lateinit var commandLineRunner: ApplicationRunner

    @Autowired private lateinit var orderService: OrderServiceImpl
    @Autowired private lateinit var orderProductRepository: OrderProductRepository
    @Autowired private lateinit var userServiceImpl: UserServiceImpl
    private lateinit var authentication: Authentication

    @BeforeEach
    fun init() {
//        commandLineRunner.run()
        authentication = AuthClientDTO(
            0,
            "12345678912",
            password = "123"
        ).toAuthentication()
        ReactiveSecurityContextHolder.withAuthentication(authentication)
    }

    @Test
    @Throws(Exception::class)
    fun thatCommandLineRunnerDoesStuff() {
        commandLineRunner.run()
    }

    @Test
    @Throws(Exception::class)
    fun test1() {
        var orderRequestDTO = OrderMock.mockOrderRequest()
        val response = orderService.createOrder(OrderMock.mockOrderRequest(), authentication)
        println(response.map { it.orderDTO?.id })
    }
}