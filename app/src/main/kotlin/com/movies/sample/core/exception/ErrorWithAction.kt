package com.movies.sample.core.exception

data class ErrorWithAction(
    val errorEntity: ErrorEntity,
    val retryListener: (() -> Unit)? = null
)