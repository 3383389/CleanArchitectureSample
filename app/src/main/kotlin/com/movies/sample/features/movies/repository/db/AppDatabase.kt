package com.movies.sample.features.movies.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.movies.sample.features.movies.repository.dto.RoomMovie
import com.movies.sample.features.movies.repository.dto.RoomMovieFavorites

@Database(
    entities = [
        RoomMovie::class,
        RoomMovieFavorites::class
    ],
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun moviesRepository(): MoviesDao

    companion object {
        const val DATABASE_VERSION = 1
    }
}