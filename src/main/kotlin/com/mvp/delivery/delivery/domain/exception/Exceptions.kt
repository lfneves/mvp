package com.mvp.delivery.delivery.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

object Exceptions {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class RequestedElementNotFoundException(message: String): RuntimeException(message)

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    class BadCredentialsException(message: String): RuntimeException(message)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class NotFoundException(message: String): RuntimeException(message)
}