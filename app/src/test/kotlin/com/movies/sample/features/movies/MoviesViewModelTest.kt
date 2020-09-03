package com.movies.sample.features.movies

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.movies.sample.AndroidTest
import com.movies.sample.core.interactor.Result
import com.movies.sample.features.movies.moviesList.MovieEntity
import com.movies.sample.features.movies.moviesList.MoviesViewModel
import com.movies.sample.features.movies.moviesList.interactor.RemoveMovieFromFavorites
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
            context() as Application,
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
        given { runBlocking { getLocalMovies.run(eq(any())) } }.willReturn(Result.Success(MutableLiveData(moviesList)))

        moviesViewModel.movies.observeForever {
            it!!.size shouldEqualTo 2
            it[0].id shouldEqualTo 0
            it[0].poster shouldEqualTo "IronMan"
            it[1].id shouldEqualTo 1
            it[1].poster shouldEqualTo "Batman"
        }
        runBlocking { moviesViewModel.loadMovies() }
    }
}