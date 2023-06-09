package com.mvp.delivery.delivery.repository


import com.mvp.delivery.delivery.model.Address
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : ReactiveCrudRepository<Address?, Long?>