package com.movies.sample.features.movies.moviesTabs

import android.os.Bundle
import android.view.View
import com.movies.sample.R
import com.movies.sample.core.extension.viewModel
import com.movies.sample.core.navigation.Navigator
import com.movies.sample.core.platform.BaseFragment
import com.movies.sample.core.platform.BaseViewPagerAdapter
import com.movies.sample.features.movies.moviesList.MoviesFragment
import kotlinx.android.synthetic.main.fragment_movies_tabs.*
import javax.inject.Inject

class MoviesTabsFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    lateinit var pagerAdapter: BaseViewPagerAdapter

    override fun layoutId() = R.layout.fragment_movies_tabs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        pagerAdapter = BaseViewPagerAdapter(childFragmentManager)
        pagerAdapter.addFragWithTitle(MoviesFragment.forMovies(), getString(R.string.movies_screen_title))
        pagerAdapter.addFragWithTitle(MoviesFragment.forMoviesFavorites(), getString(R.string.fav_movies_screen_title))
        viewpager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewpager)
    }
}
