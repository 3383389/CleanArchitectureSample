package com.movies.sample.features.movies.moviesList

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movies.sample.core.interactor.Result
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.core.platform.BaseViewModel
import com.movies.sample.features.movies.moviesList.interactor.AddMovieToFavorites
import com.movies.sample.features.movies.moviesList.interactor.GetFavoriteMovies
import com.movies.sample.features.movies.moviesList.interactor.GetLocalMovies
import com.movies.sample.features.movies.moviesList.interactor.UpdateMovies
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

    private lateinit var _movies: LiveData<List<MovieEntity>>
    val moviesMediatorLiveData = MediatorLiveData<List<MovieEntity>>()

    private var state: String = MoviesFragment.STATE_MOVIES // state by default
    val moviesLiveDataInitialized = MutableLiveData<Boolean>(false)

    fun changeState(state: String) {
        this.state = state
    }

    fun initMoviesLiveData() {
        when (state) {
            MoviesFragment.STATE_MOVIES ->
                viewModelScope.launch {
                    getLocalMovies(UseCase.None()) {
                        when (it) {
                            is Result.Success -> handleMovieList(it.data)
                            is Result.Error -> handleErrors(it) { initMoviesLiveData() }
                        }
                    }
                }
            MoviesFragment.STATE_MOVIES_FAVORITES ->
                viewModelScope.launch {
                    getFavoriteMovies(UseCase.None()) {
                        when (it) {
                            is Result.Success -> handleMovieList(it.data)
                            is Result.Error -> handleErrors(it) { initMoviesLiveData() }
                        }
                    }
                }
        }

    }

    fun loadMovies() {
        viewModelScope.launch {
            showLoading()
            delay(2222)
            updateMovies(UseCase.None()) {
                when (it) {
                    is Result.Error -> handleErrors(it) { loadMovies() }
                }
                hideLoading()
            }
        }
    }

    private fun handleMovieList(movies: LiveData<List<MovieEntity>>) {
        this._movies = movies
        moviesMediatorLiveData.addSource(_movies) { moviesMediatorLiveData.value = it }
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
                    is Result.Success -> {
                    }
                    is Result.Error -> handleErrors(it) { addFavorites(movie) }
                }
            }
        }
    }

    private fun removeMovieFromFavorites(movieId: Int) {
        viewModelScope.launch {
            removeMovieFromFavorites(RemoveMovieFromFavorites.Params(movieId)) {
                when (it) {
                    is Result.Success -> {
                    }
                    is Result.Error -> handleErrors(it) { removeMovieFromFavorites(movieId) }
                }
            }
        }
    }
}