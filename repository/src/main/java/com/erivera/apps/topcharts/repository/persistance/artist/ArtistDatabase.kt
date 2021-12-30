package com.erivera.apps.topcharts.repository.persistance.artist

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Artist::class],
    version = 1
)
abstract class ArtistDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME: String = "artist_database"
    }

    abstract fun getArtistDao(): ArtistDao
}