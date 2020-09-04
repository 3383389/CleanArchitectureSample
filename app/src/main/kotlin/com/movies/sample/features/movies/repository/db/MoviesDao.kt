package com.movies.sample.features.movies.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.movies.sample.features.movies.repository.dto.RoomMovie
import com.movies.sample.features.movies.repository.dto.RoomMovieFavorites
import com.movies.sample.features.movies.repository.dto.RoomMovieWithFavorite

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(articles: List<RoomMovie>)

    @Transaction
    @Query(
        "SELECT movies.*, movies_favorite.movieId " +
                "FROM movies " +
                "LEFT JOIN movies_favorite ON movies.id = movies_favorite.movieId"
    )
    fun allMoviesWithFavorites(): LiveData<List<RoomMovieWithFavorite>>

    @Transaction
    @Query(
        "SELECT movies.*, movies_favorite.movieId " +
                "FROM movies " +
                "LEFT JOIN movies_favorite ON movies.id = movies_favorite.movieId " +
                "WHERE movies_favorite.movieId IS NOT NULL"
    )
    fun allFavoriteMoviesLD(): LiveData<List<RoomMovieWithFavorite>>

    @Query("SELECT EXISTS(SELECT 1 FROM movies_favorite WHERE movieId = :id LIMIT 1)")
    fun isFavorite(id: Int): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteMovie(movie: RoomMovieFavorites)

    @Query("DELETE FROM movies_favorite WHERE movieId = :id")
    fun deleteFavoriteMovie(id: Int)

}