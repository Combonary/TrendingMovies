package com.example.trendingmovies.view.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.trendingmovies.R
import com.example.trendingmovies.model.Movie
import com.example.trendingmovies.utils.Constants
import com.example.trendingmovies.utils.GenreMap
import com.example.trendingmovies.view.MovieDetailActivity
import kotlinx.android.synthetic.main.movie_item.view.*

/**
 * adapter for list of movies recyclerview
 */
class MovieListAdapter(private val context: Context, private val list: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieListAdapter.MovieVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieVH(context, view)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(newList: List<Movie>) {
        list.clear()
        list.addAll(newList)
        Log.d("MovieListAdapter", list.toString())
        notifyDataSetChanged()
        Log.d("MovieListAdapter", list.toString())
    }

    class MovieVH(private val context: Context, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("movie_id", movie.id)
                context.startActivity(intent)
            }
            itemView.movie_item_name_text_view.text = movie.title

            Glide.with(context).load(Constants.IMAGE_URL + movie.poster_path).apply(
                RequestOptions().override(350, 700).fitCenter().placeholder(
                    R.drawable.placeholder
                )
            ).into(itemView.movie_poster_img_view)

            itemView.movie_item_genre_text_view.text = GenreMap.getGenre(movie.genre_ids)
        }
    }
}