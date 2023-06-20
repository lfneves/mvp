package com.mvp.delivery.domain.client.service.user

import com.mvp.delivery.domain.client.model.user.UserDTO
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.user.UserEntity
import com.mvp.delivery.infrastruture.repository.user.IAddressRepository
import com.mvp.delivery.infrastruture.repository.user.IUserRepository
import com.mvp.delivery.utils.Sha512PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class UserServiceImpl @Autowired constructor(
    @Autowired userRepository: IUserRepository,
    @Autowired addressRepository: IAddressRepository,
    private val passwordEncoder: PasswordEncoder = Sha512PasswordEncoder(),
) : IUserService {
    private val userRepository: IUserRepository
    private val addressRepository: IAddressRepository

    init {
        this.userRepository = userRepository
        this.addressRepository = addressRepository
    }

    override fun getUserById(id: Int): Mono<UserDTO> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("User not found")))
            .flatMap { user ->
                addressRepository.findById(user.idAddress!!).map { address ->
                    user.toVO(user, address)
                }
            }
    }

    override fun saveInitialUser(userEntity: UserEntity): Mono<UserEntity> {
        userEntity.password = passwordEncoder.encode(userEntity.password)
        return userRepository
            .save(userEntity)
            .block()
            .toMono()
    }

    override fun saveUser(userEntity: UserEntity): Mono<UserEntity> {
        userEntity.password = passwordEncoder.encode(userEntity.password)
        return userRepository
            .save(userEntity)
            .doOnSubscribe { return@doOnSubscribe }
    }

    override fun signup(user: UserDTO): Mono<UserDTO> {
        user.password = passwordEncoder.encode(user.password)
       return saveUserWithAddress(user)
    }

    private fun saveUserWithAddress(user: UserDTO): Mono<UserDTO> {
        return addressRepository.save(user.address!!)
            .map { address ->
                user.copy(idAddress = address.id)
            }.flatMap {userDTO ->
                userRepository.save(userDTO.toEntity())
                    .map { it.toVO() }
            }
    }

    override fun updateUser(id: Int, userDTO: UserDTO): Mono<UserDTO> {
        return getUserById(id)
            .flatMap{ user ->
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
                        it.toVO(address)
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
//        if (userRepository.existsByUsername(newUsername).awaitSingle()) {
//            throw usernameAlreadyInUseException()
//        }
        user.cpf = newCpf
    }

    private fun updateEmail(user: UserDTO, newEmail: String) {
        if (user.email == newEmail) {
            return
        }
//        if (userRepository.existsByEmail(newEmail).awaitSingle()) {
//            throw emailAlreadyInUseException()
//        }
        user.email = newEmail
    }

    override fun deleteUser(id: Int): Mono<Void> {
        // delete user with address
        return userRepository.findById(id)
            .flatMap { user ->
                if (user.idAddress == null) return@flatMap userRepository.deleteById(id)
                userRepository.deleteById(id)
                    .then(addressRepository.deleteById(user.idAddress!!))
            }
    }

    override fun getUsers(): Flux<UserDTO> {
        return userRepository
            .findAll()
            .flatMap{ user ->
                addressRepository.findById(user?.idAddress!!).map { address ->
                    return@map user.toVO(user, address!!)
                }
            }
    }

    // Used in the development process should not be used in production
    override fun deleteAllUsers(): Mono<Void> {
         return userRepository
             .deleteAll()
             .block().
             toMono()
    }
}