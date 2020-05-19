package com.movies.sample.features.movies.repository.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.movies.sample.features.movies.moviesList.MovieEntity

@Entity(tableName = "movies")
data class RoomMovie(
    @PrimaryKey
    val id: Int,
    val poster: String
)
