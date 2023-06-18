package com.mvp.delivery.delivery.infrastruture.repository.user


import com.mvp.delivery.delivery.infrastruture.entity.user.AddressEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IAddressRepository : ReactiveCrudRepository<AddressEntity?, Long?>