package com.movies.sample.features.movies.moviesList

import com.movies.sample.core.exception.Failure.FeatureFailure

class MovieFailure {
    class ListNotAvailable : FeatureFailure()
    class NonExistentMovie : FeatureFailure()
}

