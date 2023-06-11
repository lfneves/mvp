package com.mvp.delivery.delivery.service

import com.mvp.delivery.delivery.client.UserWebClient
import com.mvp.delivery.delivery.exception.NotFoundException
import com.mvp.delivery.delivery.model.User
import com.mvp.delivery.delivery.repository.IAddressRepository
import com.mvp.delivery.delivery.repository.IUserRepository
import com.mvp.delivery.delivery.utils.Sha512PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@Service
class UserServiceImpl(
    userWebClient: UserWebClient,
    userRepository: IUserRepository,
    addressRepository: IAddressRepository,
    private val passwordEncoder: PasswordEncoder = Sha512PasswordEncoder(),
) : UserService {
    private val userWebClient: UserWebClient
    @Autowired
    private val userRepository: IUserRepository
    private val addressRepository: IAddressRepository

    init {
        this.userWebClient = userWebClient
        this.userRepository = userRepository
        this.addressRepository = addressRepository
    }

    override fun getUserById(id: Int): Mono<User> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
            .flatMap { user ->
                if (user?.addressId == null) {
                    return@flatMap user?.let { Mono.just(it) }
                }
                addressRepository.findById(user.addressId!!).map { address ->
                    if (address != null) {
                        user.address = address
                    }
                    user
                }
            }
    }

    override fun saveUser(user: User): Mono<User> {
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user).toMono()
    }

    fun saveUserWithAddress(user: User): Mono<User> {
        return addressRepository.save(user.address).flatMap { address ->
            user.addressId = address.id
            user.password = passwordEncoder.encode(user.password)
            userRepository.save(user)
        }
    }

    override fun updateUser(id: Int, user: User): Mono<User> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
            .flatMap { userFlat ->
                userFlat.id = user.id
                updateUserWithAddress(userFlat)
            }
    }

    private fun updateUserWithAddress(userDTO: User): Mono<out User?> {
        return addressRepository.findById(userDTO.addressId!!).flatMap { address ->
            address!!.id = userDTO.addressId
            address.city = userDTO.address.city
            address.street = userDTO.address.street
            address.state = userDTO.address.state
            addressRepository.save(address).flatMap { address1 ->
                userDTO.addressId = address1.id
                userRepository.save(userDTO)
            }
        }
    }

    override fun deleteUser(id: Int): Mono<Void> {
        // delete user with address
        return userRepository.findById(id).flatMap { user ->
            if (user.addressId == null) return@flatMap userRepository.deleteById(id)
            addressRepository.deleteById(user.addressId!!).then(userRepository.deleteById(id))
        }
    }

    override fun getGuestUserById(id: Int): Mono<User> {
        return userWebClient.retrieveGuestUser(id)
    }

    override fun getUsers(): Flux<User> {
        return userRepository
            .findAll()
            .map{ it }
    }

    override fun deleteAllUsers(): Mono<Void> {
         return userRepository.deleteAll()
    }
}