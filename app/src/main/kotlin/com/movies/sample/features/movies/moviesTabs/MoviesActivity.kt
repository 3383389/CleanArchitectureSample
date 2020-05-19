package com.movies.sample.features.movies.moviesTabs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import com.movies.sample.R
import com.movies.sample.core.platform.BaseActivity
import com.movies.sample.features.movies.moviesList.MoviesFragment
import kotlinx.android.synthetic.main.fragment_movies_tabs.*


class MoviesActivity : BaseActivity() {

    private var imageTransitionName: String? = null
    private var iconTransitionName: String? = null

    override fun fragment() = MoviesTabsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransitionCallback()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        imageTransitionName = data?.getStringExtra(EXTRA_SHARED_IMAGE_NAME)
        iconTransitionName = data?.getStringExtra(EXTRA_SHARED_ICON_NAME)
    }

    private fun setTransitionCallback() {
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View?>?) {
                super.onMapSharedElements(names, sharedElements)
                if (imageTransitionName != null && iconTransitionName != null) { // map only reenter transition
                    val tabFragment =
                        this@MoviesActivity.supportFragmentManager.findFragmentById(R.id.fragmentContainer) as MoviesTabsFragment
                    val listFragment = tabFragment.pagerAdapter.instantiateItem(
                        tabFragment.viewpager,
                        tabFragment.viewpager.currentItem
                    ) as MoviesFragment?

                    names?.clear()
                    sharedElements?.clear()

                    listFragment?.getTransitionView(imageTransitionName!!, EXTRA_SHARED_IMAGE_NAME)?.let {
                        sharedElements?.put(imageTransitionName!!, it)
                        names?.add(imageTransitionName!!)
                    }
                    listFragment?.getTransitionView(iconTransitionName!!, EXTRA_SHARED_ICON_NAME)?.let {
                        sharedElements?.put(iconTransitionName!!, it)
                        names?.add(iconTransitionName!!)
                    }
                    imageTransitionName = null
                    iconTransitionName = null
                }
            }
        })
    }

    companion object {
        const val EXTRA_SHARED_IMAGE_NAME = "EXTRA_SHARED_IMAGE_NAME"
        const val EXTRA_SHARED_ICON_NAME = "EXTRA_SHARED_ICON_NAME"
        const val MOVIE_DETAILS_REQUEST_CODE = 3423

        fun callingIntent(context: Context) = Intent(context, MoviesActivity::class.java)
    }
}
