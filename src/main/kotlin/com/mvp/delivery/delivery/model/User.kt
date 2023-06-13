package com.mvp.delivery.delivery.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tb_client")
class User {
    @Id
    var id: Int? = null
    var name: String? = null
    var email: String? = null
    var cpf: String? = null
    var password: String? = null

    @Column("id_address")
    var idAddress: Long? = null

    @Transient
    var address: Address = Address()

    constructor()

    constructor(name: String?, password: String) {
        this.name = name
        this.password = password
    }
}