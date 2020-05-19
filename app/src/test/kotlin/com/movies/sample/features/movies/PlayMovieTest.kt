package com.movies.sample.features.movies

import androidx.lifecycle.viewModelScope
import com.movies.sample.AndroidTest
import com.movies.sample.core.navigation.Navigator
import com.movies.sample.core.platform.TestViewModel
import com.movies.sample.features.movies.details.interactor.PlayMovie
import com.nhaarman.mockito_kotlin.verify
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
        val viewModel = TestViewModel()

        playMovie(viewModel.viewModelScope, params)

        verify(navigator).openVideo(context, VIDEO_URL)
    }
}
