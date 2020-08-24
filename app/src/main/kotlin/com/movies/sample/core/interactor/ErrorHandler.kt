package com.movies.sample.core.interactor

interface ErrorHandler {

    fun getError(throwable: Throwable): ErrorEntity
}