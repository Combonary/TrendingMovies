package com.example.trendingmovies.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingmovies.R
import com.example.trendingmovies.model.Movie
import com.example.trendingmovies.model.ServerResult
import com.example.trendingmovies.view.adapters.MovieListAdapter
import com.example.trendingmovies.viewmodel.MoviesListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MoviesListActivity : AppCompatActivity() {
    private val TAG: String = MoviesListActivity::class.java.simpleName

    private val list = ArrayList<Movie>()
    private val listViewModel by viewModels<MoviesListViewModel>()
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.main_activity_title)
        val layoutManager = LinearLayoutManager(this)
        movie_list_recycler_view.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val dividerItemDecoration = DividerItemDecoration(
            movie_list_recycler_view.context,
            layoutManager.orientation
        )

        movie_list_recycler_view.addItemDecoration(dividerItemDecoration)
        movieListAdapter = MovieListAdapter(this, list)
        movie_list_recycler_view.adapter = movieListAdapter

        initUi()
        Log.d(TAG, list.toString())
    }

    private fun initUi(){
        listViewModel.movieList.observe(this) { result ->

            when (result.status) {

                ServerResult.Status.LOADING -> {
                    movie_list_progress_bar.visibility = View.VISIBLE
                }

                ServerResult.Status.SUCCESS -> {
                    result.data?.results?.let { updatedList ->
                        Log.d(TAG, updatedList.toString())
                        movieListAdapter.updateData(updatedList)
                    }
                    movie_list_progress_bar.visibility = View.GONE
                }

                ServerResult.Status.ERROR -> {
                    result.message?.let {
                        displayErrorMessage(it)
                    }
                    movie_list_progress_bar.visibility = View.GONE
                }
            }
        }
    }

    private fun displayErrorMessage(errorMessage: String){
        val errorSnackBar = Snackbar.make(list_container, errorMessage, Snackbar.LENGTH_LONG)
        errorSnackBar.show()
    }


}