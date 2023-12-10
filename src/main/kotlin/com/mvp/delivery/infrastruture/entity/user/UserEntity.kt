package com.mvp.delivery.infrastruture.entity.user

import com.mvp.delivery.domain.model.user.UserDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.CascadeType
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tb_client")
data class UserEntity (
    @Id
    @Schema(hidden = true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = [CascadeType.PERSIST])
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var cpf: String? = null,
    var password: String? = null,

    @Column("id_address")
    @Schema(hidden = true)
    var idAddress: Long? = null,
) {
    fun toDTO(): UserDTO {
        return UserDTO(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password,
            address = null
        )
    }

    fun toDTO(userEntity: UserEntity, address: AddressEntity? = null): UserDTO {
        return UserDTO(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password,
            address = address
        )
    }

    fun toDTO(address: AddressEntity? = null): UserDTO {
        return UserDTO(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password,
            address = address
        )
    }
}