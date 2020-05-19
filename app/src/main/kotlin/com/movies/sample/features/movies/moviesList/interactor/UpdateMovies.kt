package com.movies.sample.features.movies.moviesList.interactor

import com.movies.sample.core.interactor.UseCase
import com.movies.sample.core.interactor.UseCase.None
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class UpdateMovies
@Inject constructor(private val moviesRepository: MoviesRepository) : UseCase<Boolean, None>() {

    override suspend fun run(params: None) = moviesRepository.updateMovies()
}
