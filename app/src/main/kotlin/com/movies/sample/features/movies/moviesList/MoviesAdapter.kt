package com.movies.sample.features.movies.moviesList

import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.movies.sample.R
import com.movies.sample.core.extension.inflate
import com.movies.sample.core.extension.loadFromUrl
import com.movies.sample.core.navigation.Navigator
import kotlinx.android.synthetic.main.row_movie.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class MoviesAdapter
@Inject constructor() : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    internal var collection: List<MovieEntity> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    internal var clickListener: (MovieEntity, Navigator.Extras) -> Unit = { _, _ -> }
    internal var clickFavoriteListener: (MovieEntity) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.row_movie))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position], clickListener, clickFavoriteListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            movieView: MovieEntity,
            clickListener: (MovieEntity, Navigator.Extras) -> Unit,
            clickFavoriteListener: (MovieEntity) -> Unit
        ) {
            itemView.moviePoster.loadFromUrl(movieView.poster)
            itemView.fav_iv.setImageResource(if (movieView.isFavorite) R.drawable.ic_star_full else R.drawable.ic_star)
            itemView.fav_iv.setOnClickListener {
                clickFavoriteListener(movieView)
            }
            itemView.setOnClickListener {
                clickListener(
                    movieView,
                    Navigator.Extras(
                        arrayOf(
                            Pair<View, String>(
                                itemView.moviePoster,
                                it.context.getString(R.string.movie_transition_poster, movieView.id)
                            ),
                            Pair<View, String>(
                                itemView.fav_iv,
                                it.context.getString(R.string.movie_transition_favorite_icon, movieView.id)
                            )
                        )
                    )
                )
            }
        }
    }
}
