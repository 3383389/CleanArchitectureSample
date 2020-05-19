package com.movies.sample.features.movies.details

import android.content.Context
import android.content.Intent
import com.movies.sample.core.platform.BaseActivity
import com.movies.sample.features.movies.moviesList.MovieEntity

class MovieDetailsActivity : BaseActivity() {

    override fun fragment() = MovieDetailsFragment.forMovie(intent.getParcelableExtra(INTENT_EXTRA_PARAM_MOVIE))

    companion object {

        private const val INTENT_EXTRA_PARAM_MOVIE = "INTENT_PARAM_MOVIE"
        fun callingIntent(context: Context, movie: MovieEntity): Intent {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_MOVIE, movie)
            return intent
        }

    }
}
