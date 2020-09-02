package com.movies.sample.core.exception

import com.movies.sample.core.exception.ErrorEntity

interface ErrorHandler {

    fun getError(throwable: Throwable): ErrorEntity
}