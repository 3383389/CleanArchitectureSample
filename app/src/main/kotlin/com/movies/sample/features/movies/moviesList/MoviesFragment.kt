package com.movies.sample.features.movies.moviesList

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.movies.sample.R
import com.movies.sample.core.exception.ErrorEntity
import com.movies.sample.core.exception.ErrorWithAction
import com.movies.sample.core.extension.*
import com.movies.sample.core.navigation.Navigator
import com.movies.sample.core.platform.BaseFragment
import com.movies.sample.features.movies.moviesList.MovieError.ListNotAvailable
import com.movies.sample.features.movies.moviesTabs.MoviesActivity
import kotlinx.android.synthetic.main.fragment_movies_list.*
import kotlinx.android.synthetic.main.row_movie.view.*
import javax.inject.Inject


class MoviesFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private lateinit var moviesViewModel: MoviesViewModel

    override fun layoutId() = R.layout.fragment_movies_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        moviesViewModel = viewModel(viewModelFactory) {
            observe(movies, ::renderMoviesList)
            observe(progressVisibility, ::setProgressVisibility)
            error(failure, ::handleError)
            changeState(arguments?.getString(PARAM_STATE) ?: STATE_MOVIES)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        loadMoviesList()
    }

    fun getTransitionView(transitionName: String, extraKey: String): View? {
        val id = transitionName.toNumericString().toInt()
        var index = -1
        moviesAdapter.collection.forEachIndexed { i, movieEntity ->
            if (id == movieEntity.id) {
                index = i
            }
        }
        val holder = movieList?.findViewHolderForAdapterPosition(index) as MoviesAdapter.ViewHolder?

        return when (extraKey) {
            MoviesActivity.EXTRA_SHARED_IMAGE_NAME -> holder?.itemView?.moviePoster
            MoviesActivity.EXTRA_SHARED_ICON_NAME -> holder?.itemView?.fav_iv
            else -> null
        }
    }

    private fun initializeView() {
        movieList.layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(
            2,
            androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
        )
        movieList.adapter = moviesAdapter
        setListeners()
    }

    private fun setListeners() {
        moviesAdapter.clickListener = { movie, navigationExtras ->
            navigator.showMovieDetails(activity!!, movie, navigationExtras)
        }
        moviesAdapter.clickFavoriteListener = { movie ->
            moviesViewModel.onFavoritesClicked(movie)
        }
    }

    private fun loadMoviesList() {
        moviesViewModel.loadMovies()
    }

    private fun renderMoviesList(movies: List<MovieEntity>?) {
        if (movies.isNullOrEmpty().not()) {
            emptyView.invisible()
            movieList.visible()
        } else {
            emptyView.visible()
            movieList.invisible()
        }
        moviesAdapter.collection = movies.orEmpty()
    }

    private fun handleError(error: ErrorWithAction?) {
        when (error?.errorEntity) {
            is ErrorEntity.Network -> renderError(R.string.failure_network_connection, error.retryListener)
            is ErrorEntity.ServerError -> renderError(R.string.failure_server_error, error.retryListener)
            is ListNotAvailable -> renderError(R.string.failure_movies_list_unavailable, error.retryListener)
        }
    }

    private fun renderError(@StringRes message: Int, retryListener: (() -> Unit)?) {
        notifyWithAction(message, R.string.action_refresh, retryListener)
    }

    companion object {
        private const val PARAM_STATE = "param_state"
        const val STATE_MOVIES = "type_movies"
        const val STATE_MOVIES_FAVORITES = "state_movies_favorites"

        fun forMovies(): MoviesFragment {
            val movieDetailsFragment = MoviesFragment()
            movieDetailsFragment.arguments = Bundle().apply {
                putString(PARAM_STATE, STATE_MOVIES)
            }
            return movieDetailsFragment
        }

        fun forMoviesFavorites(): MoviesFragment {
            val movieDetailsFragment = MoviesFragment()
            movieDetailsFragment.arguments = Bundle().apply {
                putString(PARAM_STATE, STATE_MOVIES_FAVORITES)
            }
            return movieDetailsFragment
        }
    }
}
