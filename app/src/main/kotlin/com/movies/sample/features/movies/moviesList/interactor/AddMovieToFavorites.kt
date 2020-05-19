package com.movies.sample.features.movies.moviesList.interactor

import com.movies.sample.core.exception.Failure
import com.movies.sample.core.functional.Either
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.moviesList.MovieEntity
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class AddMovieToFavorites
@Inject constructor(private val moviesRepository: MoviesRepository) :
    UseCase<UseCase.None, AddMovieToFavorites.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        moviesRepository.addMovieToFavorites(params.movie)
        return Either.Right(None())
    }

    data class Params(val movie: MovieEntity)
}
