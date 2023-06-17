package com.mvp.delivery.delivery.model

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tb_client")
data class User (
    @Id
    @Schema(hidden = true)
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var cpf: String? = null,
    var password: String? = null,

    @Column("id_address")
    @Schema(hidden = true)
    var idAddress: Long? = null,

//    @Transient
    var address: Address? = null
)