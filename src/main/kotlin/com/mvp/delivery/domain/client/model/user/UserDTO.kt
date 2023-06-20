package com.mvp.delivery.domain.client.model.user

import com.mvp.delivery.infrastruture.entity.user.AddressDTO
import com.mvp.delivery.infrastruture.entity.user.UserEntity

data class UserDTO (
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var cpf: String? = null,
    var password: String? = null,
    var idAddress: Long? = null,

    var address: AddressDTO? = AddressDTO()
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password
        )
    }
}