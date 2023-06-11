package com.mvp.delivery.delivery.repository

import com.mvp.delivery.delivery.model.auth.ApplicationSecretAuthorityEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface IApplicationSecretAuthorityRepository: R2dbcRepository<ApplicationSecretAuthorityEntity, String>