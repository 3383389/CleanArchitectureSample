package com.movies.sample.features.movies.moviesList

import com.movies.sample.core.exception.Failure
import com.movies.sample.core.functional.Either
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class RemoveMovieFromFavorites
@Inject constructor(private val moviesRepository: MoviesRepository) :
    UseCase<UseCase.None, RemoveMovieFromFavorites.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        moviesRepository.removeMovieFromFavorites(params.movieId)
        return Either.Right(None())
    }

    data class Params(val movieId: Int)
}
