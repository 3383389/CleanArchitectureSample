package com.movies.sample.features.movies

import com.movies.sample.AndroidTest
import com.movies.sample.core.navigation.Navigator
import com.movies.sample.features.movies.interactors.PlayMovie
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class PlayMovieTest : AndroidTest() {

    private val VIDEO_URL = "https://www.youtube.com/watch?v=bVeVMFlwUb8"

    private lateinit var playMovie: PlayMovie

    private val context = context()

    @Mock private lateinit var navigator: Navigator

    @Before fun setUp() {
        playMovie =
            PlayMovie(context, navigator)
    }

    @Test fun `should play movie trailer`() {
        val params = PlayMovie.Params(VIDEO_URL)

        runBlocking { playMovie(params) }

        verify(navigator).openVideo(context, VIDEO_URL)
    }
}
