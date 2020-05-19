package com.movies.sample.features.movies.repository.dto

import com.movies.sample.core.extension.empty
import com.movies.sample.features.movies.details.MovieDetailsEntity

data class RetrofitMovieDetails(
    private val id: Int,
    private val title: String,
    private val poster: String,
    private val summary: String,
    private val cast: String,
    private val director: String,
    private val year: Int,
    private val trailer: String
) {

    companion object {
        fun empty() = RetrofitMovieDetails(
            0, String.empty(), String.empty(), String.empty(), String.empty(), String.empty(), 0, String.empty()
        )
    }

    fun toMovieDetails() = MovieDetailsEntity(
        id = id,
        title = title,
        poster = poster,
        summary = summary,
        cast = cast,
        director = director,
        year = year,
        trailer = trailer
    )
}
