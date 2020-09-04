package com.movies.sample.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.movies.sample.core.exception.ErrorWithAction

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <L : LiveData<ErrorWithAction>> LifecycleOwner.error(liveData: L, body: (ErrorWithAction?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}