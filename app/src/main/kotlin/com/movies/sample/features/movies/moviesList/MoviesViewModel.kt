package com.movies.sample.features.movies.moviesList

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.movies.sample.core.interactor.Result
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.core.platform.BaseViewModel
import com.movies.sample.features.movies.entities.MovieEntity
import com.movies.sample.features.movies.interactors.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesViewModel
@Inject constructor(
    application: Application,
    private val getLocalMovies: GetLocalMovies,
    private val updateMovies: UpdateMovies,
    private val getFavoriteMovies: GetFavoriteMovies,
    private val addMovieToFavorites: AddMovieToFavorites,
    private val removeMovieFromFavorites: RemoveMovieFromFavorites
) : BaseViewModel(application) {

    private var state: String = MoviesFragment.STATE_MOVIES // state by default

    val movies: LiveData<List<MovieEntity>> = liveData {
        emit(emptyList())
        // get movies from DB
        val result = when (state) {
            MoviesFragment.STATE_MOVIES -> getLocalMovies.run(UseCase.None())
            MoviesFragment.STATE_MOVIES_FAVORITES -> getFavoriteMovies.run(UseCase.None())
            else -> throw IllegalStateException("MoviesViewModel wrong state")
        }
        when (result) {
            is Result.Success -> emitSource(result.data)
            is Result.Error -> handleErrors(result)
        }
    }

    fun changeState(state: String) {
        this.state = state
    }

    fun loadMovies() {
        viewModelScope.launch {
            showLoading()
            delay(1000) // increase server delay for testing UI
            updateMovies(UseCase.None()) {
                when (it) {
                    is Result.Error -> handleErrors(it) { loadMovies() }
                }
                hideLoading()
            }
        }
    }

    fun onFavoritesClicked(movie: MovieEntity) {
        if (movie.isFavorite) {
            removeMovieFromFavorites(movie.id)
        } else {
            addFavorites(movie)
        }
    }

    private fun addFavorites(movie: MovieEntity) {
        viewModelScope.launch {
            addMovieToFavorites(AddMovieToFavorites.Params(movie)) {
                when (it) {
                    is Result.Error -> handleErrors(it) { addFavorites(movie) }
                }
            }
        }
    }

    private fun removeMovieFromFavorites(movieId: Int) {
        viewModelScope.launch {
            removeMovieFromFavorites(RemoveMovieFromFavorites.Params(movieId)) {
                when (it) {
                    is Result.Error -> handleErrors(it) { removeMovieFromFavorites(movieId) }
                }
            }
        }
    }
}