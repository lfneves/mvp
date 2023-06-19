package com.mvp.delivery.helpers

import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.utils.Sha512PasswordEncoder

object UserSamples {
    private const val SAMPLE_USERNAME = "123456789000"
    private const val SAMPLE_EMAIL = "testemail@gmail.com"
    private const val SAMPLE_PASSWORD = "testpassword"
    private const val SAMPLE_USER_ID = 9999
    private val passwordService = Sha512PasswordEncoder()

    fun sampleUserRegistrationRequest() = UserDTO(
        cpf = SAMPLE_USERNAME,
        email = SAMPLE_EMAIL,
        password = SAMPLE_PASSWORD,
    )

    fun sampleUserAuthenticationRequest() = UserDTO(
        email = SAMPLE_EMAIL,
        password = SAMPLE_PASSWORD,
    )

    private fun sampleUser(passwordService: Sha512PasswordEncoder) = UserDTO(
        id = SAMPLE_USER_ID,
        cpf = SAMPLE_USERNAME,
        email = SAMPLE_EMAIL,
        password = passwordService.encode(SAMPLE_PASSWORD)
    )

    fun sampleUser() = sampleUser(passwordService)

    fun sampleUpdateUserRequest() = UserDTO(
        email = "newemail@gmail.com",
        cpf = "123456789000",
        password = "new password",
    )
}