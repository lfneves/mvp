package com.mvp.delivery.delivery.repository.auth

import org.springframework.data.r2dbc.repository.R2dbcRepository

interface IApplicationSecretAuthorityRepository: R2dbcRepository<ApplicationSecretAuthorityEntity, String>