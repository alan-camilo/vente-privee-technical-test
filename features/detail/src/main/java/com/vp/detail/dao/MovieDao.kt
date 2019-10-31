package com.vp.detail.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.detail.entity.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM movieentity")
    fun getAll(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieEntity: MovieEntity)

    @Query("DELETE from movieentity WHERE title=:title")
    fun delete(title: String): Int
}