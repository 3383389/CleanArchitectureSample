package com.movies.sample.features.movies

import com.movies.sample.AndroidTest
import com.movies.sample.core.exception.Failure.NetworkConnection
import com.movies.sample.core.exception.Failure.ServerError
import com.movies.sample.core.extension.empty
import com.movies.sample.core.functional.Either
import com.movies.sample.core.functional.Either.Right
import com.movies.sample.core.platform.NetworkHandler
import com.movies.sample.features.movies.details.MovieDetailsEntity
import com.movies.sample.features.movies.repository.MoviesRepository
import com.movies.sample.features.movies.repository.MoviesRepositoryImpl
import com.movies.sample.features.movies.repository.db.MoviesDao
import com.movies.sample.features.movies.repository.dto.RetrofitMovie
import com.movies.sample.features.movies.repository.dto.RetrofitMovieDetails
import com.movies.sample.features.movies.repository.network.MoviesService
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Call
import retrofit2.Response


class MoviesRepositoryTest : AndroidTest() {

    private lateinit var repository: MoviesRepository

    @Mock
    private lateinit var networkHandler: NetworkHandler
    @Mock
    private lateinit var service: MoviesService
    @Mock
    private lateinit var moviesDao: MoviesDao

    @Mock
    private lateinit var moviesCall: Call<List<RetrofitMovie>>
    @Mock
    private lateinit var moviesResponse: Response<List<RetrofitMovie>>
    @Mock
    private lateinit var movieDetailsCall: Call<RetrofitMovieDetails>
    @Mock
    private lateinit var movieDetailsResponse: Response<RetrofitMovieDetails>

    @Before
    fun setUp() {
        repository = MoviesRepositoryImpl(networkHandler, service, moviesDao)
    }

    @Test
    fun `should get movie list from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { moviesResponse.body() }.willReturn(listOf(RetrofitMovie(1, "poster")))
        given { moviesResponse.isSuccessful }.willReturn(true)
        given { moviesCall.execute() }.willReturn(moviesResponse)
        given { service.movies() }.willReturn(moviesCall)

        service.movies().execute().body() shouldEqual listOf(RetrofitMovie(1, "poster"))

        verify(service).movies()
    }

    @Test
    fun `movies service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val movies = repository.updateMovies()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movies service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val movies = repository.updateMovies()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movies service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { moviesResponse.isSuccessful }.willReturn(false)

        val movies = repository.updateMovies()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `should return empty movie details by default`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { movieDetailsResponse.body() }.willReturn(null)
        given { movieDetailsResponse.isSuccessful }.willReturn(true)
        given { movieDetailsCall.execute() }.willReturn(movieDetailsResponse)
        given { service.movieDetails(1) }.willReturn(movieDetailsCall)

        val movieDetails = repository.movieDetails(1)

        movieDetails shouldEqual Right(MovieDetailsEntity.empty())
        verify(service).movieDetails(1)
    }

    @Test
    fun `should get movie details from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { movieDetailsResponse.body() }.willReturn(
            RetrofitMovieDetails(
                8, "title", String.empty(), String.empty(),
                String.empty(), String.empty(), 0, String.empty()
            )
        )
        given { movieDetailsResponse.isSuccessful }.willReturn(true)
        given { movieDetailsCall.execute() }.willReturn(movieDetailsResponse)
        given { service.movieDetails(1) }.willReturn(movieDetailsCall)

        val movieDetails = repository.movieDetails(1)

        movieDetails shouldEqual Right(
            MovieDetailsEntity(
                8, "title", String.empty(), String.empty(),
                String.empty(), String.empty(), 0, String.empty()
            )
        )
        verify(service).movieDetails(1)
    }

    @Test
    fun `movie details service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val movieDetails = repository.movieDetails(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movie details service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val movieDetails = repository.movieDetails(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `movie details service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movieDetails = repository.movieDetails(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `movie details request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movieDetails = repository.movieDetails(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }
}