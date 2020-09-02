package com.movies.sample.features.movies

import com.movies.sample.UnitTest
import com.movies.sample.core.interactor.Result
import com.movies.sample.features.movies.details.MovieDetailsEntity
import com.movies.sample.features.movies.details.interactor.GetMovieDetails
import com.movies.sample.features.movies.repository.MoviesRepository
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetMovieDetailsTest : UnitTest() {

    private val MOVIE_ID = 1

    private lateinit var getMovieDetails: GetMovieDetails

    @Mock
    private lateinit var moviesRepository: MoviesRepository

    @Before
    fun setUp() {
        getMovieDetails =
            GetMovieDetails(
                moviesRepository
            )
    }

    @Test
    fun `should get data from repository`() {
        runBlocking {
            getMovieDetails.run(GetMovieDetails.Params(MOVIE_ID))

            verify(moviesRepository).movieDetails(MOVIE_ID)
            verifyNoMoreInteractions(moviesRepository)
        }
    }
}
