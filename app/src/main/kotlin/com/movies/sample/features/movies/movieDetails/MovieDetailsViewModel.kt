package com.movies.sample.features.movies.movieDetails

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.movies.sample.core.interactor.Result
import com.movies.sample.core.platform.BaseViewModel
import com.movies.sample.features.movies.interactors.GetMovieDetails
import com.movies.sample.features.movies.interactors.GetMovieDetails.Params
import com.movies.sample.features.movies.interactors.PlayMovie
import com.movies.sample.features.movies.entities.MovieDetailsEntity
import com.movies.sample.features.movies.entities.MovieEntity
import com.movies.sample.features.movies.interactors.RemoveMovieFromFavorites
import com.movies.sample.features.movies.interactors.AddMovieToFavorites
import com.movies.sample.features.movies.interactors.IsMovieFavorite
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

    private var movieId: Int = -1
    var movieDetails: MutableLiveData<MovieDetailsEntity> = MutableLiveData()

    val isFavorite: LiveData<Boolean> = liveData {
        when (val result = isMovieFavorite.run(IsMovieFavorite.Params(movieId))) {
            is Result.Success -> emitSource(result.data)
            is Result.Error -> {
                handleErrors(result)
                emit(false)
            }
        }
    }

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
    }

    fun loadMovieDetails() {
        viewModelScope.launch {
            showLoading()
            getMovieDetails(Params(movieId)) {
                when (it) {
                    is Result.Success -> handleMovieDetails(it.data)
                    is Result.Error -> handleErrors(it) { loadMovieDetails() }
                }
            }
            hideLoading()
        }
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