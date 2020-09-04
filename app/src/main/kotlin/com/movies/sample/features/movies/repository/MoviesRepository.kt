package com.movies.sample.features.movies.repository

import androidx.lifecycle.LiveData
import com.movies.sample.core.interactor.Result
import com.movies.sample.features.movies.entities.MovieDetailsEntity
import com.movies.sample.features.movies.entities.MovieEntity

interface MoviesRepository {
    suspend fun loadMovies(): Result<Boolean>
    suspend fun movieDetails(movieId: Int): Result<MovieDetailsEntity>
    fun movies(): Result<LiveData<List<MovieEntity>>>
    fun favoriteMovies(): Result<LiveData<List<MovieEntity>>>
    fun addMovieToFavorites(movie: MovieEntity): Result<Boolean>
    fun removeMovieFromFavorites(movieId: Int): Result<Boolean>
    fun isFavorite(id: Int): Result<LiveData<Boolean>>
}
