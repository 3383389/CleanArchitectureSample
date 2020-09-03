package com.movies.sample.features.movies.moviesList.interactor

import com.movies.sample.core.interactor.Result
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class RemoveMovieFromFavorites
@Inject constructor(private val moviesRepository: MoviesRepository) :
    UseCase<Boolean, RemoveMovieFromFavorites.Params>() {

    override suspend fun run(params: Params): Result<Boolean> {
        return moviesRepository.removeMovieFromFavorites(params.movieId)
    }

    data class Params(val movieId: Int)
}
