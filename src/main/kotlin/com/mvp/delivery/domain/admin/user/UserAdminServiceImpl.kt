package com.mvp.delivery.domain.admin.user

import com.mvp.delivery.domain.client.model.auth.AuthenticationVO
import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.domain.client.model.user.UsernameDTO
import com.mvp.delivery.domain.client.service.auth.validator.AuthValidatorService
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.user.UserEntity
import com.mvp.delivery.infrastruture.repository.user.AddressRepository
import com.mvp.delivery.infrastruture.repository.user.UserRepository
import com.mvp.delivery.utils.Sha512PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class UserAdminServiceImpl @Autowired constructor(
    @Autowired userRepository: UserRepository,
    @Autowired addressRepository: AddressRepository
) : UserAdminService {
    private val userRepository: UserRepository
    private val addressRepository: AddressRepository

    init {
        this.userRepository = userRepository
        this.addressRepository = addressRepository
    }

    override fun getUsers(): Flux<UserDTO> {
        return userRepository
            .findAll()
            .flatMap{ user ->
                addressRepository.findById(user?.idAddress!!)
                    .map { address ->
                        return@map user.toDTO(user, address!!)
                    }
            }
    }

    // Used in the development process should not be used in production
    override fun deleteAllUsers(): Mono<Void> {
         return userRepository
             .deleteAll()
    }
}