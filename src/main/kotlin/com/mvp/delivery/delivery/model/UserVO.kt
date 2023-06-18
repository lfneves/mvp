package com.mvp.delivery.delivery.model

import com.mvp.delivery.delivery.model.entity.User


data class UserVO (
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var cpf: String? = null,
    var password: String? = null,
    var idAddress: Long? = null,

    var address: Address? = Address()
) {
    fun toEntity(): User {
        return User(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password
        )
    }
}