package com.mvp.delivery.application.unit.user

import com.mvp.delivery.domain.model.user.UserDTO
import com.mvp.delivery.domain.service.client.auth.validator.AuthValidatorService
import com.mvp.delivery.domain.service.client.user.UserServiceImpl
import com.mvp.delivery.infrastruture.entity.user.UserEntity
import com.mvp.delivery.infrastruture.repository.user.AddressRepository
import com.mvp.delivery.infrastruture.repository.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest
class UserRepositoryTest {
    private val userRepository: UserRepository = mockk()
    private val addressRepository: AddressRepository = mockk()
    private val authValidatorService: AuthValidatorService = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()

    private val userService = UserServiceImpl(userRepository, addressRepository, authValidatorService, passwordEncoder)

    @Test
    fun `test saveUser`() {
        // Mocked user data
        val userDTO = UserDTO(
            id = 1,
            name = "Test User",
            email = "test@example.com",
            password = "password123"
        )

        val userEntity = userDTO.toEntity()

        every { userRepository.deleteAll() } returns Mono.empty()
        every { userRepository.save(userEntity) } returns Mono.just(userDTO.toEntity())

        val setup: Flux<UserEntity> = userRepository.deleteAll().thenMany(userRepository.save(userEntity))

        StepVerifier
            .create(setup)
            .expectNextCount(1)
            .verifyComplete()
    }

    @Test
    fun `find user by username`() {
        val USERNAME = "99999999999"
        // Mocked user data
        val userDTO = UserDTO(
            id = 1,
            name = "Test User",
            email = "test@example.com",
            password = "password123",
            cpf = USERNAME
        )

        val userEntity = userDTO.toEntity()

        every { userRepository.deleteAll() } returns Mono.empty()
        every { userRepository.save(userEntity) } returns Mono.just(userDTO.toEntity())
        every { userRepository.findByUsernameWithAddress(USERNAME) } returns Mono.just(userDTO.toEntity())

        val setup: Mono<UserEntity> = userRepository.save(userEntity)
        val find: Mono<UserEntity> = userRepository.findByUsernameWithAddress(USERNAME)
        val composite: Publisher<UserEntity> = Mono
            .from(setup)
            .then(find)
        StepVerifier
            .create(composite)
            .consumeNextWith { account: UserEntity ->
                assertNotNull(account.id)
                assertEquals(account.cpf, USERNAME)
                assertEquals(account.name, "Test User")
            }
            .verifyComplete()
    }
}
