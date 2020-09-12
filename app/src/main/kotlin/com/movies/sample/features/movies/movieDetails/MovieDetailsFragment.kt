package com.movies.sample.features.movies.movieDetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.movies.sample.R
import com.movies.sample.core.exception.ErrorEntity
import com.movies.sample.core.exception.ErrorWithAction
import com.movies.sample.core.extension.*
import com.movies.sample.core.platform.BaseFragment
import com.movies.sample.features.movies.entities.MovieDetailsEntity
import com.movies.sample.features.movies.entities.MovieEntity
import com.movies.sample.features.movies.entities.MovieError
import com.movies.sample.features.movies.MoviesActivity
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment() {

    @Inject
    lateinit var movieDetailsAnimator: MovieDetailsAnimator

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    override fun layoutId() = R.layout.fragment_movie_details

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { movieDetailsAnimator.postponeEnterTransition(it) }

        movieDetailsViewModel = viewModel(viewModelFactory) {
            setupMovieId((arguments?.get(PARAM_MOVIE) as MovieEntity).id)
            observe(movieDetails, ::renderMovieDetails)
            observe(isFavorite, ::renderMovieIsFavorite)
            error(failure, ::handleError)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = (arguments?.get(PARAM_MOVIE) as MovieEntity)

        if (firstTimeCreated(savedInstanceState)) {
            movieDetailsViewModel.loadMovieDetails()
            postponeEnterTransition(movie)
        } else {
            cancelEnterTransition(movie)
        }
        setListeners()
    }

    override fun onBackPressed() {
        // put transition names is result intent
        val intent = Intent().apply {
            putExtra(MoviesActivity.EXTRA_SHARED_IMAGE_NAME, moviePoster.transitionName)
            putExtra(MoviesActivity.EXTRA_SHARED_ICON_NAME, fav_iv.transitionName)
        }
        activity?.setResult(Activity.RESULT_OK, intent)

        movieDetailsAnimator.fadeInvisible(scrollView, movieDetails)
        if (moviePlay.isVisible())
            moviePlay.invisible()
        else
            movieDetailsAnimator.cancelTransition(moviePoster)
    }

    private fun cancelEnterTransition(movie: MovieEntity) {
        movieDetailsAnimator.scaleUpView(moviePlay)
        movieDetailsAnimator.cancelTransition(moviePoster)
        moviePoster.loadFromUrl(movie.poster)
    }

    private fun postponeEnterTransition(movie: MovieEntity) {
        activity?.let {
            moviePoster.loadUrlAndPostponeEnterTransition(movie.poster, it)

            // set shared transition names
            ViewCompat.setTransitionName(
                fav_iv,
                getString(R.string.movie_transition_favorite_icon, movie.id)
            )
            ViewCompat.setTransitionName(
                moviePoster,
                getString(R.string.movie_transition_poster, movie.id)
            )
        }
    }

    private fun setListeners() {
        fav_iv.setOnClickListener { movieDetailsViewModel.onFavoritesClicked() }
    }

    private fun renderMovieIsFavorite(isFavorite: Boolean?) {
        fav_iv.setImageResource(if (isFavorite == true) R.drawable.ic_star_full else R.drawable.ic_star)
    }

    private fun renderMovieDetails(movie: MovieDetailsEntity?) {
        movie?.let {
            with(movie) {
                requireActivity().toolbar.title = title
                movieSummary.text = summary
                movieCast.text = cast
                movieDirector.text = director
                movieYear.text = year.toString()
                moviePlay.setOnClickListener { movieDetailsViewModel.playMovie(trailer) }
            }
        }
        movieDetailsAnimator.fadeVisible(scrollView, movieDetails)
        movieDetailsAnimator.scaleUpView(moviePlay)
    }

    private fun handleError(error: ErrorWithAction?) {
        when (error?.errorEntity) {
            is ErrorEntity.Network -> {
                notify(R.string.failure_network_connection)
                close()
            }
            is ErrorEntity.ServerError -> {
                notify(R.string.failure_server_error)
                close()
            }
            is MovieError.NonExistentMovie -> {
                notify(R.string.failure_movie_non_existent)
                close()
            }
        }
    }

    companion object {
        private const val PARAM_MOVIE = "param_movie"

        fun forMovie(movie: MovieEntity): MovieDetailsFragment {
            val movieDetailsFragment = MovieDetailsFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_MOVIE, movie)
            movieDetailsFragment.arguments = arguments

            return movieDetailsFragment
        }
    }
}
