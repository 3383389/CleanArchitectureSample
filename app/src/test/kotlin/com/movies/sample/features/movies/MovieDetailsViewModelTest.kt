package com.movies.sample.features.movies

import android.app.Application
import com.movies.sample.AndroidTest
import com.movies.sample.core.interactor.Result
import com.movies.sample.features.movies.details.MovieDetailsEntity
import com.movies.sample.features.movies.details.MovieDetailsViewModel
import com.movies.sample.features.movies.details.interactor.GetMovieDetails
import com.movies.sample.features.movies.details.interactor.PlayMovie
import com.movies.sample.features.movies.moviesList.RemoveMovieFromFavorites
import com.movies.sample.features.movies.moviesList.interactor.AddMovieToFavorites
import com.movies.sample.features.movies.moviesList.interactor.IsMovieFavorite
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class MovieDetailsViewModelTest : AndroidTest() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    @Mock
    private lateinit var getMovieDetails: GetMovieDetails

    @Mock
    private lateinit var isMovieFavorite: IsMovieFavorite

    @Mock
    private lateinit var playMovie: PlayMovie

    @Mock
    private lateinit var addMovieToFavorites: AddMovieToFavorites

    @Mock
    private lateinit var removeMovieFromFavorites: RemoveMovieFromFavorites

    @Before
    fun setUp() {
        movieDetailsViewModel = MovieDetailsViewModel(
            application = context() as Application,
            getMovieDetails = getMovieDetails,
            isMovieFavorite = isMovieFavorite,
            playMovie = playMovie,
            addMovieToFavorites = addMovieToFavorites,
            removeMovieFromFavorites = removeMovieFromFavorites
        )
    }

    @Test
    fun `loading movie details should update live data`() {
        val movieDetails = MovieDetailsEntity(
            0,
            "IronMan",
            "poster",
            "summary",
            "cast",
            "director",
            2018,
            "trailer"
        )
        given { runBlocking { getMovieDetails.run(eq(any())) } }.willReturn(Result.Success(movieDetails))

        movieDetailsViewModel.movieDetails.observeForever {
            with(it!!) {
                id shouldEqualTo 0
                title shouldEqualTo "IronMan"
                poster shouldEqualTo "poster"
                summary shouldEqualTo "summary"
                cast shouldEqualTo "cast"
                director shouldEqualTo "director"
                year shouldEqualTo 2018
                trailer shouldEqualTo "trailer"
            }
        }

        runBlocking { movieDetailsViewModel.loadMovieDetails() }
    }
}