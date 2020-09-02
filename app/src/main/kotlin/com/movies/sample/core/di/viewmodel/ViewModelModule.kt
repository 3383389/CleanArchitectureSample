package com.movies.sample.core.di.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.movies.sample.features.movies.details.MovieDetailsViewModel
import com.movies.sample.features.movies.moviesList.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.AndroidViewModelFactory

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindsMoviesViewModel(moviesViewModel: MoviesViewModel): AndroidViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindsMovieDetailsViewModel(movieDetailsViewModel: MovieDetailsViewModel): AndroidViewModel
}