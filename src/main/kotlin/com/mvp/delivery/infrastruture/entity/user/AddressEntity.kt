package com.mvp.delivery.infrastruture.entity.user

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tb_address")
data class AddressEntity (
    @Id
    @Schema(hidden = true)
    var id: Long? = null,
    var street: String? = null,
    var city: String? = null,
    var state: String? = null,

    @Column("postal_code")
    var postalCode: String? =null
) {
    fun updateUserEntity(addressEntity: AddressEntity, request: AddressEntity) {
        request.id?.let { addressEntity.id = it }
        request.street?.let { addressEntity.street = it }
        request.city?.let { addressEntity.city = it }
        request.state?.let { addressEntity.state = it }
    }
}