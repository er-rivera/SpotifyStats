package com.erivera.apps.topcharts.repository.persistance.tracks

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.erivera.apps.topcharts.repository.persistance.utl.StringListConverter

@Database(
    entities = [Track::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class TrackDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME: String = "track_database"
    }

    abstract fun getTrackDao(): TrackDao
}