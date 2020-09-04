package com.movies.sample.core.exception

interface ErrorHandler {

    fun getError(throwable: Throwable): ErrorEntity
}