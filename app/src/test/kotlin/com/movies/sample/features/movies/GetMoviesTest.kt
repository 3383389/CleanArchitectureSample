package com.movies.sample.features.movies

import androidx.lifecycle.MutableLiveData
import com.movies.sample.UnitTest
import com.movies.sample.core.interactor.Result
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.features.movies.entities.MovieEntity
import com.movies.sample.features.movies.interactors.GetMovies
import com.movies.sample.features.movies.repository.MoviesRepository
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetMoviesTest : UnitTest() {

    private lateinit var getMovies: GetMovies

    @Mock
    private lateinit var moviesRepository: MoviesRepository

    @Before
    fun setUp() {
        getMovies = GetMovies(
            moviesRepository
        )
        given { moviesRepository.movies() }.willReturn(Result.Success(MutableLiveData(listOf(MovieEntity.empty()))))
    }

    @Test
    fun `should get data from repository`() {
        runBlocking { getMovies.run(UseCase.None()) }

        verify(moviesRepository).movies()
        verifyNoMoreInteractions(moviesRepository)
    }
}
