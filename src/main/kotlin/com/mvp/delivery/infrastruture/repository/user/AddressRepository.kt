package com.mvp.delivery.infrastruture.repository.user

import com.mvp.delivery.infrastruture.entity.user.AddressDTO
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : ReactiveCrudRepository<AddressDTO?, Long?>