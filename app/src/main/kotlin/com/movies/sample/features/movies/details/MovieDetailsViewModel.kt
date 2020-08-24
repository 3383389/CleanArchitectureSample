package com.movies.sample.features.movies.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movies.sample.core.interactor.Result
import com.movies.sample.core.platform.BaseViewModel
import com.movies.sample.features.movies.details.interactor.GetMovieDetails
import com.movies.sample.features.movies.details.interactor.GetMovieDetails.Params
import com.movies.sample.features.movies.details.interactor.PlayMovie
import com.movies.sample.features.movies.moviesList.MovieEntity
import com.movies.sample.features.movies.moviesList.RemoveMovieFromFavorites
import com.movies.sample.features.movies.moviesList.interactor.AddMovieToFavorites
import com.movies.sample.features.movies.moviesList.interactor.IsMovieFavorite
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel
@Inject constructor(
    application: Application,
    private val getMovieDetails: GetMovieDetails,
    private val isMovieFavorite: IsMovieFavorite,
    private val playMovie: PlayMovie,
    private val addMovieToFavorites: AddMovieToFavorites,
    private val removeMovieFromFavorites: RemoveMovieFromFavorites
) : BaseViewModel(application) {

    var movieDetails: MutableLiveData<MovieDetailsEntity> = MutableLiveData()

    val variableIsFavoriteInitialized = MutableLiveData<Boolean>(false)
    lateinit var isFavorite: LiveData<Boolean>

    private var movieId: Int = -1

    fun playMovie(url: String) {
        viewModelScope.launch {
            playMovie(PlayMovie.Params(url)) {
                when (it) {
                    is Result.Error -> handleErrors(it) { playMovie(url) }
                }
            }
        }
    }

    fun setupMovieId(id: Int) {
        movieId = id
        viewModelScope.launch {
            isMovieFavorite(IsMovieFavorite.Params(movieId)) {
                when (it) {
                    is Result.Success -> handleIsFavorite(it.data)
                    is Result.Error -> handleErrors(it) { setupMovieId(id) }
                }
            }
        }
    }

    fun loadMovieDetails() {
        viewModelScope.launch {
            getMovieDetails(Params(movieId)) {
                when (it) {
                    is Result.Success -> handleMovieDetails(it.data)
                    is Result.Error -> handleErrors(it) { loadMovieDetails() }
                }
            }
        }
    }

    private fun handleIsFavorite(isFavorite: LiveData<Boolean>) {
        this.isFavorite = isFavorite
        this.variableIsFavoriteInitialized.value = true
    }

    private fun handleMovieDetails(movie: MovieDetailsEntity) {
        this.movieDetails.value = MovieDetailsEntity(
            movie.id, movie.title, movie.poster,
            movie.summary, movie.cast, movie.director, movie.year, movie.trailer
        )
    }

    fun onFavoritesClicked() {
        movieDetails.value?.let {
            if (isFavorite.value == true) {
                removeMovieFromFavorites(it.id)
            } else {
                addFavorites(it.toMovieEntity())
            }
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