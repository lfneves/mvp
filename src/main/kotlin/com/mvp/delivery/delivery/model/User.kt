package com.mvp.delivery.delivery.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("reactive_user")
class User {
    @Id
    var id: Int? = null
    var name: String? = null
    var score: Int? = null
    var password: String = ""

    @Column("address_id")
    var addressId: Long? = null

    @Transient
    var address: Address = Address()

    constructor()

    constructor(name: String?, password: String, score: Int?, addressId: Long?, address: Address) {
        this.name = name
        this.password = password
        this.score = score
        this.addressId = addressId
        this.address = address
    }

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", addressId=" + addressId +
                ", address=" + address +
                '}'
    }
}