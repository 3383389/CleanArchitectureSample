package com.movies.sample.core.platform

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movies.sample.core.exception.ErrorEntity
import com.movies.sample.core.exception.ErrorWithAction
import com.movies.sample.core.interactor.Result

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see ErrorEntity
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    var progressVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    var failure: MutableLiveData<ErrorWithAction> = MutableLiveData()

    protected fun showLoading() {
        progressVisibility.postValue(true)
    }

    protected fun hideLoading() {
        progressVisibility.postValue(false)
    }

    protected fun handleErrors(
        result: Result.Error,
        retryListener: (() -> Unit)? = null
    ) {
        Log.e("Error", " - ${result.error}")
        // do some logic if needed
        when (result.error) {
            is ErrorEntity.Network -> {
            }
            is ErrorEntity.ServerError -> {
            }
            else -> {
            }
        }
        // notify UI to render error message
        failure.value = ErrorWithAction(result.error, retryListener)
    }

}