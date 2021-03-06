package com.movies.sample.features.movies

import com.movies.sample.AndroidTest
import com.movies.sample.core.exception.ErrorEntity
import com.movies.sample.core.extension.empty
import com.movies.sample.core.interactor.Result
import com.movies.sample.core.platform.NetworkHandler
import com.movies.sample.features.movies.entities.MovieDetailsEntity
import com.movies.sample.features.movies.repository.MoviesRepository
import com.movies.sample.features.movies.repository.MoviesRepositoryImpl
import com.movies.sample.features.movies.repository.db.MoviesDao
import com.movies.sample.features.movies.repository.dto.RetrofitMovie
import com.movies.sample.features.movies.repository.dto.RetrofitMovieDetails
import com.movies.sample.features.movies.repository.error.GeneralErrorHandlerImpl
import com.movies.sample.features.movies.repository.network.MoviesService
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException


class MoviesRepositoryTest : AndroidTest() {

    private lateinit var repository: MoviesRepository

    @Mock
    private lateinit var networkHandler: NetworkHandler

    @Mock
    private lateinit var errorHandler: GeneralErrorHandlerImpl

    @Mock
    private lateinit var service: MoviesService

    @Mock
    private lateinit var moviesDao: MoviesDao

    @Mock
    private lateinit var moviesResponse: Deferred<List<RetrofitMovie>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = MoviesRepositoryImpl(
            service,
            moviesDao,
            errorHandler,
            networkHandler
        )
    }

    @Test
    fun `should get movie list from service`() {
        runBlocking {
            given { networkHandler.isConnected }.willReturn(true)
            given(moviesResponse.await()).willReturn(listOf(RetrofitMovie(1, "poster")))
            given { service.movies() }.willReturn(moviesResponse)

            service.movies().await() shouldEqual listOf(RetrofitMovie(1, "poster"))

            verify(service).movies()
        }
    }

    @Test
    fun `movies service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val movies = runBlocking { repository.loadMovies() }

        movies shouldBeInstanceOf Result::class.java
        movies shouldBeInstanceOf Result.Error::class.java
        (movies as Result.Error).error shouldBeInstanceOf ErrorEntity.Network::class.java
        verifyZeroInteractions(service)
    }

    @Test
    fun `movies service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val movies = runBlocking { repository.loadMovies() }

        movies shouldBeInstanceOf Result::class.java
        movies shouldBeInstanceOf Result.Error::class.java
        (movies as Result.Error).error shouldBeInstanceOf ErrorEntity.Network::class.java
        verifyZeroInteractions(service)
    }

    @Test
    fun `should return empty movie details by default`() {
        runBlocking {
            given { networkHandler.isConnected }.willReturn(true)
            given(service.movieDetails(1)).willReturn(null)

            val movieDetails = repository.movieDetails(1)

            movieDetails shouldEqual Result.Success(MovieDetailsEntity.empty())
        }
    }

    @Test
    fun `should get movie details from service`() {
        runBlocking {
            given { networkHandler.isConnected }.willReturn(true)
            given(service.movieDetails(1)).willReturn(
                RetrofitMovieDetails(
                    8, "title", String.empty(), String.empty(),
                    String.empty(), String.empty(), 0, String.empty()
                )
            )

            val movieDetails = service.movieDetails(1)

            movieDetails shouldEqual
                    RetrofitMovieDetails(
                        8, "title", String.empty(), String.empty(),
                        String.empty(), String.empty(), 0, String.empty()
                    )
            verify(service).movieDetails(1)
        }
    }

    @Test
    fun `movie details service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val movies = runBlocking { repository.movieDetails(1) }

        movies shouldBeInstanceOf Result::class.java
        movies shouldBeInstanceOf Result.Error::class.java
        (movies as Result.Error).error shouldBeInstanceOf ErrorEntity.Network::class.java
        verifyZeroInteractions(service)
    }

    @Test
    fun `movie details service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val movies = runBlocking { repository.movieDetails(1) }

        movies shouldBeInstanceOf Result::class.java
        movies shouldBeInstanceOf Result.Error::class.java
        (movies as Result.Error).error shouldBeInstanceOf ErrorEntity.Network::class.java
        verifyZeroInteractions(service)
    }

}