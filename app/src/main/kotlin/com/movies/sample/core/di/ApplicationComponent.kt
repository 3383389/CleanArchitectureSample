package com.movies.sample.core.di

import com.movies.sample.AndroidApplication
import com.movies.sample.core.di.viewmodel.ViewModelModule
import com.movies.sample.core.navigation.RouteActivity
import com.movies.sample.features.movies.movieDetails.MovieDetailsFragment
import com.movies.sample.features.movies.moviesList.MoviesFragment
import com.movies.sample.features.movies.moviesTabs.MoviesTabsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)
    fun inject(routeActivity: RouteActivity)

    fun inject(moviesFragment: MoviesFragment)
    fun inject(movieDetailsFragment: MovieDetailsFragment)
    fun inject(moviesTabsFragment: MoviesTabsFragment)
}
