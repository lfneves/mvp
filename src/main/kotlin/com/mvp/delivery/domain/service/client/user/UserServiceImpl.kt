package com.mvp.delivery.domain.service.client.user

import com.mvp.delivery.domain.model.auth.AuthenticationVO
import com.mvp.delivery.domain.model.user.UserDTO
import com.mvp.delivery.domain.model.user.UsernameDTO
import com.mvp.delivery.domain.service.client.auth.validator.AuthValidatorService
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.repository.user.AddressRepository
import com.mvp.delivery.infrastruture.repository.user.UserRepository
import com.mvp.delivery.utils.Sha512PasswordEncoder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val authValidatorService: AuthValidatorService,
    private val passwordEncoder: PasswordEncoder = Sha512PasswordEncoder(),
) : UserService {

    override fun getUserById(id: Int, authentication: Authentication): Mono<UserDTO> {
        authentication as AuthenticationVO
        if (authentication.iAuthDTO.username == null) return Mono.empty()
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("User not found")))
            .flatMap {
                authValidatorService.validate(authentication, it.toDTO())
                    .then(Mono.just(it))
            }.flatMap { user ->
                addressRepository.findById(user.idAddress!!).map { address ->
                    user.toDTO(user, address)
                }
            }
    }

    override fun getByUsername(usernameDTO: UsernameDTO, authentication: Authentication): Mono<UserDTO> {
        return userRepository.findByUsernameWithAddress(usernameDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("User not found")))
            .flatMap {
                authValidatorService.validate(authentication, it.toDTO())
                    .then(Mono.just(it))
            }.flatMap { user ->
                addressRepository.findById(user.idAddress!!).map { address ->
                    user.toDTO(user, address)
                }
            }.toMono()
    }

    fun getByUsername(username: String): Mono<UserDTO>  {
        return userRepository.findByUsernameWithAddress(username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("User not found")))
            .flatMap { user ->
                addressRepository.findById(user.idAddress!!).map { address ->
                    user.toDTO(user, address)
                }
            }
    }

    override fun saveUser(user: UserDTO): Mono<UserDTO> {
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user.toEntity())
            .map { it.toDTO() }

    }

    override fun signup(user: UserDTO): Mono<UserDTO> {
        user.password = passwordEncoder.encode(user.password)
       return saveUserWithAddress(user)
    }

    private fun saveUserWithAddress(user: UserDTO): Mono<UserDTO> {
        return addressRepository.save(user.address!!)
            .map { address ->
                user.address = address
                user.copy(idAddress = address.id)
            }.flatMap {userDTO ->
                userRepository.save(userDTO.toEntity())
                    .map {
                        it
                    }
            }.flatMap {userEntity ->
                addressRepository.findById(userEntity.idAddress!!)
                    .map { address ->
                        return@map userEntity.toDTO(userEntity, address!!)
                    }
            }
    }

    override fun updateUser(id: Int, userDTO: UserDTO, authentication: Authentication): Mono<UserDTO> {
        return getUserById(id, authentication)
            .flatMap {
                authValidatorService.validate(authentication, it)
                    .then(Mono.just(it))
            }.flatMap{ user ->
                updateUserEntity(user, userDTO)
                userRepository.save(user.toEntity())
            }.flatMap {
                addressRepository.findById(it.idAddress!!)
                    .map { address ->
                        userDTO.address?.let { updateAddress ->
                            if (address != null) {
                                updateAddress.updateUserEntity(address, updateAddress)
                                addressRepository.save(address).subscribe()
                            }
                        }
                        it.toDTO(address)
                    }
            }
    }

    private fun updateUserEntity(user: UserDTO, request: UserDTO) {
        request.id?.let { user.id = it }
        request.name?.let { user.name = it }
        request.idAddress?.let { user.idAddress = it }
        request.cpf?.let { user.cpf = it }
        request.password?.let { user.password = passwordEncoder.encode(it) }
        request.cpf?.let { updateCpf(user, it) }
        request.email?.let { updateEmail(user, it) }
    }

    private fun updateCpf(user: UserDTO, newCpf: String) {
        if (user.cpf == newCpf) {
            return
        }
        user.cpf = newCpf
    }

    private fun updateEmail(user: UserDTO, newEmail: String) {
        if (user.email == newEmail) {
            return
        }
        user.email = newEmail
    }

    override fun deleteUserById(id: Int, authentication: Authentication): Mono<Void> {
        // delete user with address
        return userRepository.findById(id)
            .flatMap {
                authValidatorService.validate(authentication, it.toDTO())
                    .then(Mono.just(it))
            }.flatMap { user ->
                if (user.idAddress == null) return@flatMap userRepository.deleteById(id)
                userRepository.deleteById(id)
                    .then(addressRepository.deleteById(user.idAddress!!))
            }
    }
}