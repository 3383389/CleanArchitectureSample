package com.movies.sample.features.movies.entities

import com.movies.sample.core.extension.empty

data class MovieDetailsEntity(
    val id: Int,
    val title: String,
    val poster: String,
    val summary: String,
    val cast: String,
    val director: String,
    val year: Int,
    val trailer: String
) {

    fun toMovieEntity(): MovieEntity =
        MovieEntity(id, poster, false)

    companion object {
        fun empty() = MovieDetailsEntity(
            0, String.empty(), String.empty(), String.empty(),
            String.empty(), String.empty(), 0, String.empty()
        )
    }
}


