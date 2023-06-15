package com.mvp.delivery.delivery.service.user

import com.mvp.delivery.delivery.exception.NotFoundException
import com.mvp.delivery.delivery.model.User
import com.mvp.delivery.delivery.repository.user.IAddressRepository
import com.mvp.delivery.delivery.repository.user.IUserRepository
import com.mvp.delivery.delivery.utils.Sha512PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class UserServiceImpl(
    userRepository: IUserRepository,
    addressRepository: IAddressRepository,
    private val passwordEncoder: PasswordEncoder = Sha512PasswordEncoder(),
) : IUserService {
    @Autowired
    private val userRepository: IUserRepository
    private val addressRepository: IAddressRepository

    init {
        this.userRepository = userRepository
        this.addressRepository = addressRepository
    }

    override fun getUserById(id: Int): Mono<User> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
            .flatMap { user ->
                if (user?.idAddress == null) {
                    return@flatMap user?.let { Mono.just(it) }
                }
                addressRepository.findById(user.idAddress!!).map { address ->
                    if (address != null) {
                        user.address = address
                    }
                    user
                }
            }
    }

    override fun saveInitialUser(user: User): Mono<User> {
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user).block().toMono()
    }
    override fun saveUser(user: User): Mono<User> {
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user).doOnSubscribe { return@doOnSubscribe }
    }

    override fun signup(user: User): Mono<User> {
        return saveUserWithAddress(user)
    }

    fun saveUserWithAddress(user: User): Mono<User> {
        return addressRepository.save(user.address!!).flatMap { address ->
            user.idAddress = address.id
            user.password = passwordEncoder.encode(user.password)
            userRepository.save(user)
        }.doOnSubscribe { return@doOnSubscribe }
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
        return addressRepository.findById(userDTO.idAddress!!).flatMap { address ->
            address!!.id = userDTO.idAddress
            address.city = userDTO.address!!.city
            address.street = userDTO.address!!.street
            address.state = userDTO.address!!.state
            address.postalCode = userDTO.address!!.postalCode
            addressRepository.save(address).flatMap { address1 ->
                userDTO.idAddress = address1.id
                userRepository.save(userDTO)
            }
        }
    }

    override fun deleteUser(id: Int): Mono<Void> {
        // delete user with address
        return userRepository.findById(id).flatMap { user ->
            if (user.idAddress == null) return@flatMap userRepository.deleteById(id)
            addressRepository.deleteById(user.idAddress!!).then(userRepository.deleteById(id))
        }
    }

    override fun getUsers(): Flux<User> {
        return userRepository
            .findAll()
            .flatMap label@{ user ->
                if (user?.idAddress == null) return@label Mono.just(user!!)
                addressRepository.findById(user.idAddress!!).map { address ->
                    user.address = address!!
                    user
                }
            }
    }

    override fun deleteAllUsers(): Mono<Void> {
         return userRepository
             .deleteAll()
             .block().
             toMono()
    }
}