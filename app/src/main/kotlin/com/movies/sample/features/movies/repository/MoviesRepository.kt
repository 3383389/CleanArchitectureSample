package com.movies.sample.features.movies.repository

import androidx.lifecycle.LiveData
import com.movies.sample.core.interactor.Result
import com.movies.sample.features.movies.details.MovieDetailsEntity
import com.movies.sample.features.movies.moviesList.MovieEntity

interface MoviesRepository {
    suspend fun updateMovies(): Result<Boolean>
    suspend fun movieDetails(movieId: Int): Result<MovieDetailsEntity>
    fun localMovies(): Result<LiveData<List<MovieEntity>>>
    fun favoriteMovies(): Result<LiveData<List<MovieEntity>>>
    fun addMovieToFavorites(movie: MovieEntity): Result<Boolean>
    fun removeMovieFromFavorites(movieId: Int): Result<Boolean>
    fun isFavorite(id: Int): Result<LiveData<Boolean>>
}
