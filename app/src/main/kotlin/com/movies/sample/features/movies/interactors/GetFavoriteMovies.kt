package com.movies.sample.features.movies.interactors

import androidx.lifecycle.LiveData
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.entities.MovieEntity
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class GetFavoriteMovies
@Inject constructor(private val moviesRepository: MoviesRepository) : UseCase<LiveData<List<MovieEntity>>, UseCase.None>() {

    override suspend fun run(params: None) = moviesRepository.favoriteMovies()
}
