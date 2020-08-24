package com.movies.sample.features.movies.repository.error

import android.util.Log
import com.movies.sample.core.interactor.ErrorEntity
import com.movies.sample.core.interactor.ErrorHandler
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
                    HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorEntity.Unauthorized

                    // not found
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

                    // access denied
                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied

                    // unavailable service
                    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable

                    HttpURLConnection.HTTP_BAD_REQUEST -> ErrorEntity.BadRequest(parseMessage(throwable))

                    // all the others will be treated as unknown error
                    else -> ErrorEntity.Unknown
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