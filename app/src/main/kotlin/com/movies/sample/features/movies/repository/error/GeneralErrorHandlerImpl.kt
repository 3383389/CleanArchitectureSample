package com.movies.sample.features.movies.repository.error

import android.util.Log
import com.movies.sample.core.exception.ErrorEntity
import com.movies.sample.core.exception.ErrorHandler
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

class GeneralErrorHandlerImpl : ErrorHandler {

    override fun getError(throwable: Throwable): ErrorEntity {
        Log.e(javaClass.name, "Error - ", throwable)
        return when (throwable) {
            is IOException -> ErrorEntity.Network
            is HttpException -> {
                when (throwable.code()) {
                    // Unauthorized
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorEntity.ServerError

                    // not found
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.ServerError

                    // access denied
                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.ServerError

                    // unavailable service
                    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServerError

                    HttpURLConnection.HTTP_BAD_REQUEST -> ErrorEntity.BadRequest(parseMessage(throwable))

                    else -> ErrorEntity.ServerError
                }
            }
            else -> ErrorEntity.Unknown
        }
    }

    private fun parseMessage(httpException: HttpException): String? {
        // todo parse message from back
        return null
    }
}