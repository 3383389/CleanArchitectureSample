package com.movies.sample.features.movies.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.movies.sample.features.movies.repository.dto.RoomMovieWithFavorite
import com.movies.sample.features.movies.repository.dto.RoomMovie
import com.movies.sample.features.movies.repository.dto.RoomMovieFavorites

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun allMoviesLD(): LiveData<List<RoomMovie>>

    @Query("SELECT * FROM movies")
    fun allMovies(): List<RoomMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(articles: List<RoomMovie>)

    @Delete
    fun deleteMovie(article: RoomMovie)


    // Favorites
    @Transaction
    @Query("SELECT * FROM movies")
    fun allMoviesWithFavorites(): LiveData<List<RoomMovieWithFavorite>>

    @Query("SELECT * FROM movies_favorite")
    fun allFavoriteMoviesLD(): LiveData<List<RoomMovieFavorites>>

    @Query("SELECT * FROM movies_favorite")
    fun allFavoriteMovies(): List<RoomMovieFavorites>

    @Query("SELECT EXISTS(SELECT 1 FROM movies_favorite WHERE id = :id LIMIT 1)")
    fun isFavorite(id: Int): LiveData<Boolean>

    @Transaction
    @Query("SELECT * FROM movies WHERE id IN (SELECT DISTINCT movies_favorite.id FROM movies_favorite)")
    fun getFavoritesMovies(): LiveData<List<RoomMovieWithFavorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteMovie(movie: RoomMovieFavorites)

    @Delete
    fun deleteFavoriteMovie(article: RoomMovieFavorites)

    @Query("DELETE FROM movies_favorite WHERE id = :id")
    fun deleteFavoriteMovie(id: Int)

}