package com.mvp.delivery.delivery.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("address")
class Address {
    @Id
    var id: Long? = null
    var street: String? = null
    var city: String? = null
    var state: String? = null

    constructor()

    constructor(city: String?) {
        this.city = city
    }

    override fun toString(): String {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}'
    }
}