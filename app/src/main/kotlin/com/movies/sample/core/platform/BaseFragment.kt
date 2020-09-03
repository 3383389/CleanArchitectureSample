package com.movies.sample.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.movies.sample.AndroidApplication
import com.movies.sample.R.color
import com.movies.sample.core.di.ApplicationComponent
import com.movies.sample.core.extension.appContext
import com.movies.sample.core.extension.falseIfNull
import com.movies.sample.core.extension.viewContainer
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as AndroidApplication).appComponent
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId(), container, false)

    open fun onBackPressed() {}

    abstract fun layoutId(): Int

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun notify(@StringRes message: Int) =
        Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()

    internal fun notifyWithAction(@StringRes message: Int, @StringRes actionText: Int, action: (() -> Unit)?) {
        val snackBar = Snackbar.make(viewContainer, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(actionText) { action?.invoke() }
        snackBar.setActionTextColor(
            ContextCompat.getColor(
                appContext,
                color.colorTextPrimary
            )
        )
        snackBar.show()
    }

    protected fun setProgressVisibility(visible: Boolean?) {
        with(activity) {
            if (this is BaseActivity) this.progress.visibility =
                if (visible.falseIfNull()) View.VISIBLE
                else View.INVISIBLE
        }
    }
}
