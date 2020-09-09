package com.movies.sample.core.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.movies.sample.core.extension.empty
import com.movies.sample.features.login.Authenticator
import com.movies.sample.features.login.LoginActivity
import com.movies.sample.features.movies.entities.MovieEntity
import com.movies.sample.features.movies.movieDetails.MovieDetailsActivity
import com.movies.sample.features.movies.MoviesActivity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Navigator
@Inject constructor(private val authenticator: Authenticator) {

    private fun showLogin(context: Context) = context.startActivity(LoginActivity.callingIntent(context))

    fun showMain(context: Context) {
        when (authenticator.userLoggedIn()) {
            true -> showMovies(context)
            false -> showLogin(context)
        }
    }

    private fun showMovies(context: Context) = context.startActivity(MoviesActivity.callingIntent(context))

    fun showMovieDetails(
        activity: androidx.fragment.app.FragmentActivity,
        movie: MovieEntity,
        navigationExtras: Extras
    ) {
        val intent = MovieDetailsActivity.callingIntent(activity, movie)
        val sharedView = navigationExtras.transitionSharedElement
        val activityOptions = ActivityOptionsCompat
            .makeSceneTransitionAnimation(activity, *sharedView)
        activity.startActivityForResult(intent, MoviesActivity.MOVIE_DETAILS_REQUEST_CODE, activityOptions.toBundle())
    }

    fun openVideo(context: Context, videoUrl: String) {
        try {
            context.startActivity(createYoutubeIntent(videoUrl))
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)))
        }
    }

    private fun createYoutubeIntent(videoUrl: String): Intent {
        val videoId = when {
            videoUrl.startsWith(VIDEO_URL_HTTP) -> videoUrl.replace(VIDEO_URL_HTTP, String.empty())
            videoUrl.startsWith(VIDEO_URL_HTTPS) -> videoUrl.replace(VIDEO_URL_HTTPS, String.empty())
            else -> videoUrl
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("force_fullscreen", true)

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.M)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return intent
    }

    class Extras(val transitionSharedElement: Array<Pair<View, String>>)

    companion object {
        private const val VIDEO_URL_HTTP = "http://www.youtube.com/watch?v="
        private const val VIDEO_URL_HTTPS = "https://www.youtube.com/watch?v="
    }
}


