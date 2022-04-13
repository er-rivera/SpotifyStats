package com.erivera.apps.topcharts.repository.persistance.artist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Query("SELECT * from artists")
    fun getArtists(): Flow<List<Artist>>

    @Query("SELECT * from artists")
    suspend fun getSingleShotArtists(): List<Artist>

    @Query("SELECT * FROM artists WHERE current_short_position > -1 ORDER BY current_short_position LIMIT :limit")
    fun getShortTermArtists(limit: Int): Flow<List<Artist>>

    @Query("SELECT * FROM artists WHERE current_mid_position > -1 ORDER BY current_mid_position LIMIT :limit")
    fun getMidTermArtists(limit: Int): Flow<List<Artist>>

    @Query("SELECT * FROM artists WHERE current_long_position > -1 ORDER BY current_long_position LIMIT :limit")
    fun getLongTermArtists(limit: Int): Flow<List<Artist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArtists(artists: List<Artist>)
}