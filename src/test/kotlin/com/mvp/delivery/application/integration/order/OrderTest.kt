package com.mvp.delivery.application.integration.order

import com.mvp.delivery.application.integration.user.UserApplicationTest
import com.mvp.delivery.domain.model.auth.AuthClientDTO
import com.mvp.delivery.domain.service.client.order.OrderServiceImpl
import com.mvp.delivery.domain.service.admin.user.UserAdminServiceImpl
import com.mvp.delivery.domain.configuration.ApplicationRunner
import com.mvp.delivery.helpers.OrderMock
import com.mvp.delivery.helpers.UserMock
import com.mvp.delivery.infrastruture.repository.order.OrderProductRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest
class OrderTest(
    @Autowired val client: WebTestClient,
) {
    private val logger = LoggerFactory.getLogger(UserApplicationTest::class.java)

    @SpyBean
    private lateinit var commandLineRunner: ApplicationRunner

    @Autowired private lateinit var orderService: OrderServiceImpl
    @Autowired private lateinit var orderProductRepository: OrderProductRepository
    @Autowired private lateinit var userServiceImpl: UserAdminServiceImpl
    private lateinit var authentication: Authentication

    val orderApiRequestTest = OrderRequestApiTest(client)

    @BeforeEach
    fun init() {
        //commandLineRunner.run()
        authentication = AuthClientDTO(
            0,
            "12345678912",
            password = "123"
        ).toAuthentication()
        ReactiveSecurityContextHolder.withAuthentication(authentication)
    }

    @Test
    @Throws(Exception::class)
    fun `Command Runner start default user and products`() {
        commandLineRunner.run()
    }

    @Test
    fun `Create Order test and validate registration expected = true`() {
        val request = OrderMock.mockOrderRequest()
        val response = orderApiRequestTest.createOrderTest(request)
        logger.info(response.toString())

        Assertions.assertThat(response.orderDTO!!.id).isNotNull
        Assertions.assertThat(response.orderDTO!!.idClient).isEqualTo(OrderMock.mockOrder().idClient)
        Assertions.assertThat(response.orderDTO!!.status).isEqualTo(OrderMock.mockOrder().status)
        Assertions.assertThat(response.orderDTO!!.isFinished).isEqualTo(OrderMock.mockOrder().isFinished)
    }

    @Test
    fun `Find Order By ID and validate expected = true`() {
        val request = UserMock.mockUser().id!!
        val response = orderApiRequestTest.getById(request)
        logger.info(response.toString())

        Assertions.assertThat(response.id).isNotNull
        Assertions.assertThat(response.idClient).isEqualTo(OrderMock.mockOrder().idClient)
        Assertions.assertThat(response.status).isEqualTo(OrderMock.mockOrder().status)
        Assertions.assertThat(response.isFinished).isEqualTo(OrderMock.mockOrder().isFinished)
    }

    @Test
    fun `Delete User By ID expected = true`() {
        val request = UserMock.mockUser().id!!
        val response = orderApiRequestTest.deleteOrderById(request)
        logger.info(response.toString())

        Assertions.assertThat(response.id).isNotNull
        Assertions.assertThat(response.idClient).isEqualTo(OrderMock.mockOrder().idClient)
        Assertions.assertThat(response.status).isEqualTo(OrderMock.mockOrder().status)
        Assertions.assertThat(response.isFinished).isEqualTo(OrderMock.mockOrder().isFinished)
    }
}