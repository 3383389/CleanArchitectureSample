package com.movies.sample.features.movies.repository.dto

import com.movies.sample.features.movies.entities.MovieEntity

data class RetrofitMovie(private val id: Int, private val poster: String) {
    fun toMovie() = MovieEntity(id, poster, false) // todo
    fun toRoomMovie() = RoomMovie(id = id, poster = poster)

    companion object {
        fun empty() = RetrofitMovie(-1, "")
    }
}
