package com.movies.sample.features.movies.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movies.sample.core.exception.Failure
import com.movies.sample.core.functional.Either
import com.movies.sample.core.platform.BaseViewModel
import com.movies.sample.features.movies.details.interactor.GetMovieDetails
import com.movies.sample.features.movies.details.interactor.GetMovieDetails.Params
import com.movies.sample.features.movies.details.interactor.PlayMovie
import com.movies.sample.features.movies.moviesList.interactor.AddMovieToFavorites
import com.movies.sample.features.movies.moviesList.interactor.IsMovieFavorite
import com.movies.sample.features.movies.moviesList.MovieEntity
import com.movies.sample.features.movies.moviesList.RemoveMovieFromFavorites
import javax.inject.Inject

class MovieDetailsViewModel
@Inject constructor(
    private val getMovieDetails: GetMovieDetails,
    private val isMovieFavorite: IsMovieFavorite,
    private val playMovie: PlayMovie,
    private val addMovieToFavorites: AddMovieToFavorites,
    private val removeMovieFromFavorites: RemoveMovieFromFavorites
) : BaseViewModel() {

    var movieDetails: MutableLiveData<MovieDetailsEntity> = MutableLiveData()

    val variableIsFavoriteInitialized = MutableLiveData<Boolean>(false)
    lateinit var isFavorite: LiveData<Boolean>

    private var movieId: Int = -1

    fun playMovie(url: String) = playMovie(viewModelScope, PlayMovie.Params(url))

    fun setupMovieId(id: Int) {
        movieId = id
        isMovieFavorite(viewModelScope, IsMovieFavorite.Params(movieId)) { it.fold(::handleFailure, ::handleIsFavorite) }
    }

    fun loadMovieDetails() =
        getMovieDetails(viewModelScope, Params(movieId)) { it.fold(::handleFailure, ::handleMovieDetails) }

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
        addMovieToFavorites(
            viewModelScope,
            AddMovieToFavorites.Params(movie)
        ) { if (it.isLeft) handleFailure((it as Either.Left<Failure>).a) }
    }

    private fun removeMovieFromFavorites(movieId: Int) {
        removeMovieFromFavorites(
            viewModelScope,
            RemoveMovieFromFavorites.Params(movieId)
        ) { if (it.isLeft) handleFailure((it as Either.Left<Failure>).a) }
    }

}