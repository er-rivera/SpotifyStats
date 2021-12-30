package com.erivera.apps.topcharts.repository.persistance.tracks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * from tracks")
    fun getTracks(): Flow<List<Track>>

    @Query("SELECT * FROM tracks WHERE current_short_position > -1 ORDER BY current_short_position LIMIT :limit")
    fun getShortTermTracks(limit: Int): Flow<List<Track>>

    @Query("SELECT * FROM tracks WHERE current_mid_position > -1 ORDER BY current_mid_position LIMIT :limit")
    fun getMidTermTracks(limit: Int): Flow<List<Track>>

    @Query("SELECT * FROM tracks WHERE current_long_position > -1 ORDER BY current_long_position LIMIT :limit")
    fun getLongTermTracks(limit: Int): Flow<List<Track>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTracks(tracks: List<Track>)
}