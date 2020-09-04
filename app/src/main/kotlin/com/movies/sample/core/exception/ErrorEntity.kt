package com.movies.sample.core.exception

sealed class ErrorEntity(open val message: String? = null) {

    class BadRequest(override val message: String? = null) : ErrorEntity(message)

    object Network : ErrorEntity()

    object ServerError : ErrorEntity()

    object Unknown : ErrorEntity()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureError : ErrorEntity()
}