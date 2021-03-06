package com.movies.sample.features.movies.repository.network

import com.movies.sample.features.movies.repository.dto.RetrofitMovie
import com.movies.sample.features.movies.repository.dto.RetrofitMovieDetails
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

internal interface MoviesApi {

    @GET(MOVIES)
    fun movies(): Deferred<List<RetrofitMovie?>?>

    @GET(MOVIE_DETAILS)
    suspend fun movieDetails(@Path(PARAM_MOVIE_ID) movieId: Int): RetrofitMovieDetails?

    companion object {
        private const val PARAM_MOVIE_ID = "movieId"
        private const val MOVIES = "movies.json"
        private const val MOVIE_DETAILS = "details/movie_0{$PARAM_MOVIE_ID}.json"
    }
}
