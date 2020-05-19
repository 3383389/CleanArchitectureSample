package com.movies.sample.features.movies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.movies.sample.core.exception.Failure
import com.movies.sample.core.functional.Either
import com.movies.sample.core.platform.NetworkHandler
import com.movies.sample.features.movies.details.MovieDetailsEntity
import com.movies.sample.features.movies.moviesList.MovieEntity
import com.movies.sample.features.movies.repository.db.MoviesDao
import com.movies.sample.features.movies.repository.dto.RetrofitMovieDetails
import com.movies.sample.features.movies.repository.dto.RoomMovie
import com.movies.sample.features.movies.repository.network.MoviesService
import retrofit2.Call
import javax.inject.Inject

class MoviesRepositoryImpl
@Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: MoviesService,
    private val database: MoviesDao
) : MoviesRepository {

    override fun updateMovies(): Either<Failure, Boolean> {
        if (networkHandler.isConnected != true) {
            return Either.Left(Failure.NetworkConnection)
        }
        val result = request(
            service.movies(),
            { list -> list.map { movie -> movie.toRoomMovie() } },
            emptyList()
        )
        return if (result.isRight) {
            database.insertMovies(
                (result as Either.Right<List<RoomMovie>>).b
            )
            Either.Right(true)
        } else {
            Either.Left(Failure.ServerError)
        }
    }

    override fun addMovieToFavorites(movie: MovieEntity): Either<Failure, Boolean> {
        database.insertFavoriteMovie(movie.toRoomFavorites())
        return Either.Right(true)
    }

    override fun removeMovieFromFavorites(movieId: Int): Either<Failure, Boolean> {
        database.deleteFavoriteMovie(movieId)
        return Either.Right(true)
    }

    override fun isFavorite(id: Int): Either<Failure, LiveData<Boolean>> {
        return Either.Right(database.isFavorite(id))
    }

    override fun localMovies(): Either<Failure, LiveData<List<MovieEntity>>> {
        val moviesLd = database.allMoviesWithFavorites()
        return Either.Right(Transformations.map(moviesLd) { input ->
            input.map { MovieEntity(it.movie.id, it.movie.poster, it.favoriteMovies.isNotEmpty()) }
        })
    }

    override fun favoriteMovies(): Either<Failure, LiveData<List<MovieEntity>>> {
        val moviesLd = database.allFavoriteMoviesLD()
        return Either.Right(Transformations.map(moviesLd) { input ->
            input.map { MovieEntity(it.id, it.poster, true) }
        })
    }

    override fun movieDetails(movieId: Int): Either<Failure, MovieDetailsEntity> {
        return when (networkHandler.isConnected) {
            true -> request(service.movieDetails(movieId), { it.toMovieDetails() }, RetrofitMovieDetails.empty())
            false, null -> Either.Left(Failure.NetworkConnection)
        }
    }

    private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> Either.Right(transform((response.body() ?: default)))
                false -> Either.Left(Failure.ServerError)
            }
        } catch (exception: Throwable) {
            Either.Left(Failure.ServerError)
        }
    }
}