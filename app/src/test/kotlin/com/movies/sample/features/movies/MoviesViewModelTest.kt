package com.movies.sample.features.movies

import androidx.lifecycle.MutableLiveData
import com.movies.sample.AndroidTest
import com.movies.sample.core.functional.Either.Right
import com.movies.sample.features.movies.moviesList.*
import com.movies.sample.features.movies.moviesList.interactor.AddMovieToFavorites
import com.movies.sample.features.movies.moviesList.interactor.GetFavoriteMovies
import com.movies.sample.features.movies.moviesList.interactor.GetLocalMovies
import com.movies.sample.features.movies.moviesList.interactor.UpdateMovies
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class MoviesViewModelTest : AndroidTest() {

    private lateinit var moviesViewModel: MoviesViewModel

    @Mock
    private lateinit var getLocalMovies: GetLocalMovies
    @Mock
    private lateinit var updateMovies: UpdateMovies
    @Mock
    private lateinit var getFavoriteMovies: GetFavoriteMovies
    @Mock
    private lateinit var addMovieToFavorites: AddMovieToFavorites
    @Mock
    private lateinit var removeMovieFromFavorites: RemoveMovieFromFavorites

    @Before
    fun setUp() {
        moviesViewModel = MoviesViewModel(
            getLocalMovies,
            updateMovies,
            getFavoriteMovies,
            addMovieToFavorites,
            removeMovieFromFavorites
        )
    }

    @Test
    fun `loading movies should update live data`() {
        val moviesList = listOf(
            MovieEntity(0, "IronMan", false),
            MovieEntity(1, "Batman", true)
        )
        given { runBlocking { getLocalMovies.run(eq(any())) } }.willReturn(Right(MutableLiveData(moviesList)))

        moviesViewModel.moviesVariableInitialized.observeForever { isInitialized ->
            if (isInitialized == true) {
                moviesViewModel.movies.observeForever {
                    it!!.size shouldEqualTo 2
                    it[0].id shouldEqualTo 0
                    it[0].poster shouldEqualTo "IronMan"
                    it[1].id shouldEqualTo 1
                    it[1].poster shouldEqualTo "Batman"
                }
            }
        }
        runBlocking { moviesViewModel.loadMovies() }
    }
}