package com.movies.sample.features.movies.repository.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class RoomMovie(
    @PrimaryKey
    val id: Int,
    val poster: String
)
