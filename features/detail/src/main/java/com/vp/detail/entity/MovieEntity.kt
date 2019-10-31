package com.vp.detail.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vp.detail.model.MovieDetail

@Entity
data class MovieEntity(
        val title: String,
        val year: String,
        val runtime: String,
        val director: String,
        val plot: String,
        val poster: String,
        var imdbId: String? = null) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun MovieDetail.toMovieEntity() = MovieEntity(this.title, this.year, this.runtime, this.director, this.plot, this.poster)
