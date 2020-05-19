package com.movies.sample.features.movies.moviesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movies.sample.core.exception.Failure
import com.movies.sample.core.functional.Either
import com.movies.sample.core.interactor.UseCase.None
import com.movies.sample.core.platform.BaseViewModel
import com.movies.sample.features.movies.moviesList.interactor.AddMovieToFavorites
import com.movies.sample.features.movies.moviesList.interactor.GetFavoriteMovies
import com.movies.sample.features.movies.moviesList.interactor.GetLocalMovies
import com.movies.sample.features.movies.moviesList.interactor.UpdateMovies
import javax.inject.Inject

class MoviesViewModel
@Inject constructor(
    private val getLocalMovies: GetLocalMovies,
    private val updateMovies: UpdateMovies,
    private val getFavoriteMovies: GetFavoriteMovies,
    private val addMovieToFavorites: AddMovieToFavorites,
    private val removeMovieFromFavorites: RemoveMovieFromFavorites
) : BaseViewModel() {

    lateinit var movies: LiveData<List<MovieEntity>>

    var state: String = MoviesFragment.STATE_MOVIES // state by default
    val moviesVariableInitialized = MutableLiveData<Boolean>(false)

    fun changeState(state: String) {
        this.state = state
    }

    fun initMoviesLiveData() {
        when (state) {
            MoviesFragment.STATE_MOVIES ->
                getLocalMovies(viewModelScope, None()) {
                    it.fold(::handleFailure, ::handleMovieList)
                }
            MoviesFragment.STATE_MOVIES_FAVORITES ->
                getFavoriteMovies(viewModelScope, None()) {
                    it.fold(::handleFailure, ::handleMovieList)
                }
        }
    }

    fun loadMovies() =
        updateMovies(viewModelScope, None()) { if (it.isLeft) handleFailure((it as Either.Left<Failure>).a) }

    private fun handleMovieList(movies: LiveData<List<MovieEntity>>) {
        this.movies = movies
        moviesVariableInitialized.value = true
    }

    fun onFavoritesClicked(movie: MovieEntity) {
        if (movie.isFavorite) {
            removeMovieFromFavorites(movie)
        } else {
            addFavorites(movie)
        }
    }

    private fun addFavorites(movie: MovieEntity) {
        addMovieToFavorites(
            viewModelScope,
            AddMovieToFavorites.Params(movie)
        ) { if (it.isLeft) handleFailure((it as Either.Left<Failure>).a) }
    }

    private fun removeMovieFromFavorites(movie: MovieEntity) {
        removeMovieFromFavorites(
            viewModelScope,
            RemoveMovieFromFavorites.Params(movie.id)
        ) { if (it.isLeft) handleFailure((it as Either.Left<Failure>).a) }
    }
}