package com.movies.sample.features.movies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.movies.sample.core.exception.ErrorEntity
import com.movies.sample.core.exception.ErrorHandler
import com.movies.sample.core.interactor.Result
import com.movies.sample.core.platform.NetworkHandler
import com.movies.sample.features.movies.entities.MovieDetailsEntity
import com.movies.sample.features.movies.entities.MovieEntity
import com.movies.sample.features.movies.repository.db.MoviesDao
import com.movies.sample.features.movies.repository.dto.RetrofitMovie
import com.movies.sample.features.movies.repository.network.MoviesService
import javax.inject.Inject

class MoviesRepositoryImpl
@Inject constructor(
    private val service: MoviesService,
    private val database: MoviesDao,
    private val errorHandler: ErrorHandler,
    private val networkHandler: NetworkHandler
) : MoviesRepository {

    override suspend fun loadMovies(): Result<Boolean> {
        if (networkHandler.isConnected != true) {
            return Result.Error(ErrorEntity.Network)
        }
        return try {
            // fetch data from backend
            val resultList = service.movies().await()?.map { movie ->
                movie?.toRoomMovie() ?: RetrofitMovie.empty().toRoomMovie()
            }
            // save to local DB
            if (resultList != null) {
                database.insertMovies(resultList)
            }
            Result.Success(true)
        } catch (throwable: Throwable) {
            Result.Error(errorHandler.getError(throwable))
        }
    }

    override fun movies(): Result<LiveData<List<MovieEntity>>> {
        return try {
            // get liveData of movies and transform to app entity
            val moviesLd = database.allMoviesWithFavorites()
            Result.Success(Transformations.map(moviesLd) { input ->
                input.map {
                    MovieEntity(
                        it.movie.id,
                        it.movie.poster,
                        it.movieId != null
                    )
                }
            })
        } catch (throwable: Throwable) {
            Result.Error(errorHandler.getError(throwable))
        }
    }

    override suspend fun movieDetails(movieId: Int): Result<MovieDetailsEntity> {
        if (networkHandler.isConnected != true) {
            return Result.Error(ErrorEntity.Network)
        }
        return try {
            val res = service.movieDetails(movieId).await()
            Result.Success(res?.toMovieDetails() ?: MovieDetailsEntity.empty())
        } catch (throwable: Throwable) {
            Result.Error(errorHandler.getError(throwable))
        }
    }

    override fun favoriteMovies(): Result<LiveData<List<MovieEntity>>> {
        return try {
            val moviesLd = database.allFavoriteMoviesLD()
            Result.Success(Transformations.map(moviesLd) { input ->
                input.map {
                    MovieEntity(
                        it.movie.id,
                        it.movie.poster,
                        it.movieId != null
                    )
                }
            })
        } catch (throwable: Throwable) {
            Result.Error(errorHandler.getError(throwable))
        }
    }

    override fun addMovieToFavorites(movie: MovieEntity): Result<Boolean> {
        return try {
            database.insertFavoriteMovie(movie.toRoomFavorites())
            Result.Success(true)
        } catch (throwable: Throwable) {
            Result.Error(errorHandler.getError(throwable))
        }
    }

    override fun removeMovieFromFavorites(movieId: Int): Result<Boolean> {
        return try {
            database.deleteFavoriteMovie(movieId)
            Result.Success(true)
        } catch (throwable: Throwable) {
            Result.Error(errorHandler.getError(throwable))
        }
    }

    override fun isFavorite(id: Int): Result<LiveData<Boolean>> {
        return try {
            Result.Success(database.isFavorite(id))
        } catch (throwable: Throwable) {
            Result.Error(errorHandler.getError(throwable))
        }
    }
}