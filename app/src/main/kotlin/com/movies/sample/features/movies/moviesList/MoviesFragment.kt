package com.movies.sample.features.movies.moviesList

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.movies.sample.R
import com.movies.sample.core.exception.Failure
import com.movies.sample.core.exception.Failure.NetworkConnection
import com.movies.sample.core.exception.Failure.ServerError
import com.movies.sample.core.extension.*
import com.movies.sample.core.navigation.Navigator
import com.movies.sample.core.platform.BaseFragment
import com.movies.sample.features.movies.moviesList.MovieFailure.ListNotAvailable
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
            observe(moviesVariableInitialized, ::subscribeOnMovies)
            failure(failure, ::handleFailure)
            changeState(arguments?.getString(PARAM_STATE) ?: STATE_MOVIES)
            initMoviesLiveData()
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
        emptyView.invisible()
        movieList.visible()
        showProgress()
        moviesViewModel.loadMovies()
    }

    private fun subscribeOnMovies(isVarInitialized: Boolean?) {
        if (isVarInitialized == true) {
            observe(moviesViewModel.movies, ::renderMoviesList)
            moviesViewModel.moviesVariableInitialized.removeObservers(this)
        }
    }

    private fun renderMoviesList(movies: List<MovieEntity>?) {
        moviesAdapter.collection = movies.orEmpty()
        hideProgress()
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is ServerError -> renderFailure(R.string.failure_server_error)
            is ListNotAvailable -> renderFailure(R.string.failure_movies_list_unavailable)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notifyWithAction(message, R.string.action_refresh, ::loadMoviesList)
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
