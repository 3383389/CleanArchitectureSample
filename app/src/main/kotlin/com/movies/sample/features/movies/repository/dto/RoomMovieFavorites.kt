package com.movies.sample.features.movies.repository.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies_favorite",
    foreignKeys = [
        ForeignKey(entity = RoomMovie::class, parentColumns = ["id"], childColumns = ["movieId"])
    ]
)
data class RoomMovieFavorites(
    @PrimaryKey
    val movieId: Int
)
