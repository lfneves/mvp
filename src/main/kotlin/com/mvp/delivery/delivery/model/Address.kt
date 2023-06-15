package com.mvp.delivery.delivery.model

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tb_address")
data class Address (
    @Id
    @Schema(hidden = true)
    var id: Long? = null,
    var street: String? = null,
    var city: String? = null,
    var state: String? = null,

    @Column("postal_code")
    var postalCode: String? =null
)