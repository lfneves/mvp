package com.mvp.delivery.delivery.service

import com.mvp.delivery.delivery.client.UserWebClient
import com.mvp.delivery.delivery.exception.NotFoundException
import com.mvp.delivery.delivery.model.User
import com.mvp.delivery.delivery.repository.AddressRepository
import com.mvp.delivery.delivery.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class UserServiceImpl(
    userWebClient: UserWebClient,
    userRepository: UserRepository,
    addressRepository: AddressRepository
) : UserService {
    private val userWebClient: UserWebClient
    private val userRepository: UserRepository
    private val addressRepository: AddressRepository

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

    override fun saveUser(user: Flux<User>): Mono<User> {
        return user
            .flatMap{userFlat -> saveUserWithAddress(userFlat)}
            .toMono()
    }

    fun saveUserWithAddress(user: User): Mono<User> {
        return addressRepository.save(user.address).flatMap { address ->
            user.addressId = address.id
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