package com.example.trendingmovies.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.trendingmovies.R
import com.example.trendingmovies.model.MovieDescription
import com.example.trendingmovies.model.ServerResult
import com.example.trendingmovies.utils.Constants
import com.example.trendingmovies.viewmodel.MovieDetailViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.activity_movie_detail.movie_list_progress_bar

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {
    private val movieDetailViewModel by viewModels<MovieDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent?.getIntExtra("movie_id", 0)?.let {
            movieDetailViewModel.getMovieDetail(it)
            initUi()
        } ?: displayErrorMessage("Server Error")

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun updateUi(movieDescription: MovieDescription) {
        title = movieDescription.title
        detail_movie_name_text_view.text = movieDescription.title
        movie_description_tv.text = movieDescription.overview

        Glide.with(this).load(Constants.IMAGE_URL + movieDescription.poster_path).apply(
            RequestOptions().override(600 * 1000).centerInside().placeholder(R.drawable.placeholder)
        ).into(detail_movie_img_view)

        val genres = mutableListOf<String>()
        movieDescription.genres.map {
            genres.add(it.name)
        }

        detail_movie_genre_text_view.text = genres.joinToString(separator = ", ")
    }

    private fun displayErrorMessage(errorMessage: String) {
        val errorSnackBar = Snackbar.make(detail_container, errorMessage, Snackbar.LENGTH_LONG)
        errorSnackBar.show()
    }

    private fun initUi() {
        movieDetailViewModel.movie.observe(this, Observer { serverResult ->
            when (serverResult.status) {
                ServerResult.Status.LOADING -> {
                    movie_list_progress_bar.visibility = View.VISIBLE
                }

                ServerResult.Status.SUCCESS -> {
                    serverResult.data?.let {
                        updateUi(it)
                    }
                    movie_list_progress_bar.visibility = View.GONE
                }

                ServerResult.Status.ERROR -> {
                    serverResult.message?.let {
                        displayErrorMessage(it)
                    }
                }
            }
        })
    }
}