package com.movies.sample.features.movies.entities

import android.os.Parcelable
import com.movies.sample.core.extension.empty
import com.movies.sample.features.movies.repository.dto.RoomMovieFavorites
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieEntity(
    val id: Int,
    val poster: String,
    var isFavorite: Boolean
) : Parcelable {

    fun toRoomFavorites(): RoomMovieFavorites = RoomMovieFavorites(id)

    companion object {
        fun empty() =
            MovieEntity(0, String.empty(), false)
    }
}
