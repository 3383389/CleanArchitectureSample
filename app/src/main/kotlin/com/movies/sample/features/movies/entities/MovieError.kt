package com.movies.sample.features.movies.entities

import com.movies.sample.core.exception.ErrorEntity

class MovieError {
    class ListNotAvailable : ErrorEntity.FeatureError()
    class NonExistentMovie : ErrorEntity.FeatureError()
    class ErrorPlaying : ErrorEntity.FeatureError()
}

