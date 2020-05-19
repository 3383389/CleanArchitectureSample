package com.movies.sample.features.movies.details.interactor

import android.content.Context
import com.movies.sample.features.movies.details.interactor.PlayMovie.Params
import com.movies.sample.core.exception.Failure
import com.movies.sample.core.functional.Either
import com.movies.sample.core.functional.Either.Right
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.core.interactor.UseCase.None
import com.movies.sample.core.navigation.Navigator
import javax.inject.Inject

class PlayMovie
@Inject constructor(private val context: Context,
                    private val navigator: Navigator) : UseCase<None, Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        navigator.openVideo(context, params.url)
        return Right(None())
    }

    data class Params(val url: String)
}
