package com.movies.sample.features.movies.details.interactor

import android.content.Context
import com.movies.sample.core.interactor.ErrorEntity
import com.movies.sample.core.interactor.Result
import com.movies.sample.core.interactor.UseCase
import com.movies.sample.core.navigation.Navigator
import com.movies.sample.features.movies.details.interactor.PlayMovie.Params
import javax.inject.Inject

class PlayMovie
@Inject constructor(
    private val context: Context,
    private val navigator: Navigator
) : UseCase<UseCase.None, Params>() {

    override suspend fun run(params: Params): Result<None> {
        return try {
            navigator.openVideo(context, params.url)
            Result.Success(None())
        } catch (throwable: Throwable) {
            Result.Error(ErrorEntity.Unknown)
        }
    }

    data class Params(val url: String)
}
