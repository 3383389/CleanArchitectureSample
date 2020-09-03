package com.movies.sample.features.movies.repository.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "movies_favorite",
    foreignKeys = [
        ForeignKey(entity = RoomMovie::class, parentColumns = ["id"], childColumns = ["id"])
    ],
    indices = [Index("id")])
data class RoomMovieFavorites(
    @PrimaryKey
    val id: Int,
    val poster: String
)
