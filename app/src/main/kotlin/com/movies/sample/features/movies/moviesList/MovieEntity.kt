package com.movies.sample.features.movies.moviesList

import android.os.Parcel
import com.movies.sample.core.extension.empty
import com.movies.sample.core.platform.KParcelable
import com.movies.sample.core.platform.parcelableCreator
import com.movies.sample.core.platform.readBoolean
import com.movies.sample.core.platform.writeBoolean
import com.movies.sample.features.movies.repository.dto.RoomMovieFavorites

data class MovieEntity(
    val id: Int,
    val poster: String,
    var isFavorite: Boolean
) : KParcelable {
    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::MovieEntity)

        fun empty() = MovieEntity(0, String.empty(), false)
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readBoolean()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(poster)
            writeBoolean(isFavorite)
        }
    }

    fun toRoomFavorites(): RoomMovieFavorites = RoomMovieFavorites(id, poster)
}
