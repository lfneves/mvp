package com.mvp.delivery.helpers

import com.mvp.delivery.domain.model.user.UserDTO
import com.mvp.delivery.utils.Sha512PasswordEncoder

object UserMock {
    private const val TEST_USERNAME = "12345678912"
    private const val TEST_NAME = "Lucas"
    private const val TEST_EMAIL = "testemail@email.com"
    private const val TEST_PASSWORD = "123"
    private const val TEST_USER_ID = 9999
    private const val TEST_ADDRESS_ID = 99L
    private const val TEST_ADDRESS_STREET = "São Paulo"
    private val passwordEncoder = Sha512PasswordEncoder()

    fun mockUserRegistrationRequest() = UserDTO(
        name = TEST_NAME,
        cpf = TEST_USERNAME,
        email = TEST_EMAIL,
        password = passwordEncoder.encode(TEST_PASSWORD)
    )

    fun mockUserAuthenticationRequest() = UserDTO(
        email = TEST_EMAIL,
        password = TEST_PASSWORD,
    )

    private fun mockUser(passwordService: Sha512PasswordEncoder) = UserDTO(
        id = TEST_USER_ID,
        name = TEST_NAME,
        cpf = TEST_USERNAME,
        email = TEST_EMAIL,
        password = passwordService.encode(TEST_PASSWORD),
        idAddress = TEST_ADDRESS_ID
    )

    fun mockUser() = mockUser(passwordEncoder)

    fun mockUpdateUserRequest() = UserDTO(
        email = "test@email.com",
        cpf = "12345678912",
        password = "1234",
    )
}