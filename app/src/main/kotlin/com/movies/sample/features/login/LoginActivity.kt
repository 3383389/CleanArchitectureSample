package com.movies.sample.features.login

import android.content.Context
import android.content.Intent
import com.movies.sample.core.platform.BaseActivity

class LoginActivity : BaseActivity() {
    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun fragment() = LoginFragment()
}
