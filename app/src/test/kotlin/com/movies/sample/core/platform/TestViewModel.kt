package com.movies.sample.core.platform

import com.movies.sample.core.exception.Failure

class TestViewModel : BaseViewModel() {
    fun handleError(failure: Failure) = handleFailure(failure)
}