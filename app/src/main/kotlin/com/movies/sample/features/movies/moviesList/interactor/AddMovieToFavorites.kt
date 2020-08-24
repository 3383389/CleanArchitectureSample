package com.movies.sample.features.movies.moviesList.interactor

import com.movies.sample.core.interactor.Result
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.moviesList.MovieEntity
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class AddMovieToFavorites
@Inject constructor(private val moviesRepository: MoviesRepository) :
    UseCase<Boolean, AddMovieToFavorites.Params>() {

    override suspend fun run(params: Params): Result<Boolean> {
        return moviesRepository.addMovieToFavorites(params.movie)
    }

    data class Params(val movie: MovieEntity)
}
