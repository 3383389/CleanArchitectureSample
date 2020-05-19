package com.movies.sample.features.movies.repository

import androidx.lifecycle.LiveData
import com.movies.sample.core.exception.Failure
import com.movies.sample.core.functional.Either
import com.movies.sample.features.movies.details.MovieDetailsEntity
import com.movies.sample.features.movies.moviesList.MovieEntity

interface MoviesRepository {
    fun movieDetails(movieId: Int): Either<Failure, MovieDetailsEntity>
    fun updateMovies(): Either<Failure, Boolean>
    fun localMovies(): Either<Failure, LiveData<List<MovieEntity>>>
    fun favoriteMovies(): Either<Failure, LiveData<List<MovieEntity>>>
    fun addMovieToFavorites(movie: MovieEntity): Either<Failure, Boolean>
    fun removeMovieFromFavorites(movieId: Int): Either<Failure, Boolean>
    fun isFavorite(id: Int): Either<Failure, LiveData<Boolean>>
}
