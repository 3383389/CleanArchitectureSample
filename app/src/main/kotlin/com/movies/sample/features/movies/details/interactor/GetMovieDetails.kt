package com.movies.sample.features.movies.details.interactor

import com.movies.sample.core.exception.Failure
import com.movies.sample.core.functional.Either
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.details.MovieDetailsEntity
import com.movies.sample.features.movies.details.interactor.GetMovieDetails.Params
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetails
@Inject constructor(private val moviesRepository: MoviesRepository) : UseCase<MovieDetailsEntity, Params>() {

    override suspend fun run(params: Params): Either<Failure, MovieDetailsEntity> {
        return moviesRepository.movieDetails(params.id)
    }

    data class Params(val id: Int)
}
