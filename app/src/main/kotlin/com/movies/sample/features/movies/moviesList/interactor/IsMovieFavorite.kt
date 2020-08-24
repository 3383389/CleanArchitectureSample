package com.movies.sample.features.movies.moviesList.interactor

import androidx.lifecycle.LiveData
import com.movies.sample.core.interactor.Result
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.repository.MoviesRepository
import javax.inject.Inject

class IsMovieFavorite
@Inject constructor(private val moviesRepository: MoviesRepository) :
    UseCase<LiveData<Boolean>, IsMovieFavorite.Params>() {

    override suspend fun run(params: Params): Result<LiveData<Boolean>> {
        return moviesRepository.isFavorite(params.id)
    }

    data class Params(val id: Int)
}
