package com.vp.detail.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.database.AppDatabase
import com.vp.detail.entity.toMovieEntity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    var imdbId: String? = null

    fun favorite(): LiveData<Boolean> = isFavorite

    fun toggleFavorite(context: Context) {
        val movieDetail = details.value ?: return
        val isFavorite = this.isFavorite.value ?: false
        if (isFavorite) {
            GlobalScope.launch(Dispatchers.IO) {
                AppDatabase.getInstance(context).movieDao().delete(movieDetail.title)
            }
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                AppDatabase.getInstance(context).movieDao().insert(
                        movieDetail.toMovieEntity().apply { imdbId = this@DetailsViewModel.imdbId }
                )
            }
        }
        this.isFavorite.postValue(!isFavorite)
    }

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails(context: Context) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(DetailActivity.queryProvider.getMovieId()).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())

                response?.body()?.title?.let {
                    title.postValue(it)
                    fetchFavorite(context, it)
                }

                loadingState.value = LoadingState.LOADED
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    fun fetchFavorite(context: Context, title: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val list = AppDatabase.getInstance(context).movieDao().getAll()
            isFavorite.postValue(list.any { it.title == title })
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}

