package com.movies.sample.features.movies.interactors

import com.movies.sample.core.interactor.Result
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.entities.MovieDetailsEntity
import com.movies.sample.features.movies.interactors.GetMovieDetails.Params
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetails
@Inject constructor(private val moviesRepository: MoviesRepository) : UseCase<MovieDetailsEntity, Params>() {

    override suspend fun run(params: Params): Result<MovieDetailsEntity> {
        return moviesRepository.movieDetails(params.id)
    }

    data class Params(val id: Int)
}
