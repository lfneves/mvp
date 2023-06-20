package com.mvp.delivery.application.user

import com.mvp.delivery.helpers.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UserApplicationTest(
    @Autowired val client: WebTestClient,
    @Autowired private val passwordEncoder: PasswordEncoder,
) {
    private val logger = LoggerFactory.getLogger(UserApplicationTest::class.java)

    val userApiRequestTest = UserRequestApiTest(client)

    @Test
    fun `Signup test and validate user registration expected = true`() {
        val request = UserMock.mockUserRegistrationRequest()
        val response = userApiRequestTest.signup(request)
        logger.info(response.toString())

        assertThat(response.name).isEqualTo(UserMock.mockUser().name)
        assertThat(response.email).isEqualTo(UserMock.mockUser().email)
        assertThat(response.cpf).isEqualTo(UserMock.mockUser().cpf)
        assertThat(response.password).isEqualTo(passwordEncoder.encode(UserMock.mockUser().password))
        assertNotNull(response.idAddress)
    }

    @Test
    fun `Find User By ID and validate expected = true`() {
        val request = UserMock.mockUser().id!!
        val response = userApiRequestTest.getById(request)
        logger.info(response.toString())

        assertThat(response.name).isEqualTo(UserMock.mockUser().name)
        assertThat(response.email).isEqualTo(UserMock.mockUser().email)
        assertThat(response.cpf).isEqualTo(UserMock.mockUser().cpf)
        assertThat(response.password).isEqualTo(passwordEncoder.encode(UserMock.mockUser().password))
        assertNotNull(response.idAddress)
    }

    @Test
    fun `Delete User By ID expected = true`() {
        val request = UserMock.mockUser().id!!
        val response = userApiRequestTest.deleteUserById(request)
        logger.info(response.toString())

        assertThat(response.name).isEqualTo(UserMock.mockUser().name)
        assertThat(response.email).isEqualTo(UserMock.mockUser().email)
        assertThat(response.cpf).isEqualTo(UserMock.mockUser().cpf)
        assertThat(response.password).isEqualTo(passwordEncoder.encode(UserMock.mockUser().password))
        assertNotNull(response.idAddress)
    }

    @Test
    fun `Update User By ID and UserDTO expected = true`() {
        val request = UserMock.mockUser()
        val response = userApiRequestTest.updateUser(request.id!!, request)
        logger.info(response.toString())

        assertThat(response.name).isEqualTo(UserMock.mockUser().name)
        assertThat(response.email).isEqualTo(UserMock.mockUser().email)
        assertThat(response.cpf).isEqualTo(UserMock.mockUser().cpf)
        assertThat(response.password).isEqualTo(passwordEncoder.encode(UserMock.mockUser().password))
        assertNotNull(response.idAddress)
    }
}