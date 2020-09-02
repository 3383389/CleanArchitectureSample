package com.movies.sample.features.movies.moviesList.interactor

import androidx.lifecycle.LiveData
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.moviesList.MovieEntity
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class GetLocalMovies
@Inject constructor(private val moviesRepository: MoviesRepository)
    : UseCase<LiveData<List<MovieEntity>>, UseCase.None>() {

    override suspend fun run(params: None) = moviesRepository.localMovies()
}
