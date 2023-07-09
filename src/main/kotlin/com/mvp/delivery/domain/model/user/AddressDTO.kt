package com.mvp.delivery.domain.model.user

import io.swagger.v3.oas.annotations.media.Schema

data class AddressDTO (

    @Schema(hidden = true)
    var id: Long? = null,
    var street: String? = null,
    var city: String? = null,
    var state: String? = null,

    var postalCode: String? =null
)