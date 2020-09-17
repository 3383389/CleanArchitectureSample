package com.movies.sample.features.movies.repository.network

import com.movies.sample.features.movies.repository.dto.RetrofitMovie
import com.movies.sample.features.movies.repository.dto.RetrofitMovieDetails
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesService
@Inject constructor(retrofit: Retrofit) : MoviesApi {
    private val moviesApi by lazy { retrofit.create(MoviesApi::class.java) }

    override fun movies(): Deferred<List<RetrofitMovie?>?> {
        return moviesApi.movies()
    }

    override suspend fun movieDetails(movieId: Int): RetrofitMovieDetails? {
        return moviesApi.movieDetails(movieId)
    }
}
