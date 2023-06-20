package com.mvp.delivery.infrastruture.entity.user

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tb_address")
data class AddressDTO (
    @Id
    @Schema(hidden = true)
    var id: Long? = null,
    var street: String? = null,
    var city: String? = null,
    var state: String? = null,

    @Column("postal_code")
    var postalCode: String? =null
) {
    fun updateUserEntity(address: AddressDTO, request: AddressDTO) {
        request.id?.let { address.id = it }
        request.street?.let { address.street = it }
        request.city?.let { address.city = it }
        request.state?.let { address.state = it }
    }
}