package com.movies.sample.core.platform

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movies.sample.AndroidApplication
import com.movies.sample.core.exception.Failure
import com.movies.sample.core.extension.toast
import com.movies.sample.core.exception.ErrorEntity
import com.movies.sample.core.interactor.Result

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleErrors(
        result: Result.Error,
        retryListener: (() -> Unit)? = null
    ) {
        Log.e("Error", " - ${result.error}")
        when (result.error) {
            is ErrorEntity.Network -> {
                showError(message = "no network", retryListener = retryListener)
            }
            is ErrorEntity.Unauthorized -> {
                // do some work with auth logic
                showError(message = "Unauthorized", retryListener = retryListener)
            }
            is ErrorEntity.BadRequest -> {
                showError(message = result.error.message, retryListener = retryListener)
            }
            is ErrorEntity.ServiceUnavailable -> {
                showError(message = "try later", retryListener = retryListener)
            }
            is ErrorEntity.Cancelled -> {
                showError(message = "Cancelled", retryListener = retryListener)
            }
            else -> {
                showError(message = "something went wrong", retryListener = retryListener)
            }
        }
    }

    protected fun showError(
        message: String?,
        cancelListener: (() -> Unit)? = null,
        retryListener: (() -> Unit)? = null
    ) {
        message?.let {
            (getApplication() as AndroidApplication).toast(it)
        }
    }
}