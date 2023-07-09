package com.mvp.delivery.domain.service.admin.user

import com.mvp.delivery.domain.model.user.UserDTO
import com.mvp.delivery.infrastruture.repository.user.AddressRepository
import com.mvp.delivery.infrastruture.repository.user.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserAdminServiceImpl(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository
): UserAdminService {

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