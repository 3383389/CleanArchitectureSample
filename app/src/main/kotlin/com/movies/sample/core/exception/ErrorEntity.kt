package com.movies.sample.core.exception

sealed class ErrorEntity (open val message: String? = null) {

    object Unauthorized : ErrorEntity()

    object Network : ErrorEntity()

    object NotFound : ErrorEntity()
    
    object AccessDenied : ErrorEntity()

    object ServiceUnavailable : ErrorEntity()

    class BadRequest(override val message: String? = null) : ErrorEntity()

    object Cancelled : ErrorEntity()

    object Unknown : ErrorEntity()

    object UnknownServiceError : ErrorEntity()
}