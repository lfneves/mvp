package com.mvp.delivery.delivery.infrastruture.entity.user

import com.mvp.delivery.delivery.domain.client.model.user.UserDTO
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tb_client")
data class UserEntity (
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
) {
    fun toVO(userEntity: UserEntity, addressEntity: AddressEntity? = null): UserDTO {
        return UserDTO(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password,
            address = addressEntity
        )
    }

    fun toVO(addressEntity: AddressEntity? = null): UserDTO {
        return UserDTO(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password,
            address = addressEntity
        )
    }
}