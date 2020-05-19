package com.movies.sample.core.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movies.sample.features.movies.details.MovieDetailsViewModel
import com.movies.sample.features.movies.moviesList.MoviesViewModel
import com.movies.sample.features.movies.moviesTabs.MoviesTabsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindsMoviesViewModel(moviesViewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindsMovieDetailsViewModel(movieDetailsViewModel: MovieDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoviesTabsViewModel::class)
    abstract fun bindsMovieTabsViewModel(moviesTabsViewModel: MoviesTabsViewModel): ViewModel
}