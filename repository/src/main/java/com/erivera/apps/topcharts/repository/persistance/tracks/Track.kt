package com.erivera.apps.topcharts.repository.persistance.tracks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo(name = "artist_names") val artistNames: List<String>,
    @ColumnInfo(name = "artist_ids") val artistIds: List<String>,
    @ColumnInfo(name = "artist_concatenated") val artistConcatenated: String,
    @ColumnInfo(name = "current_short_position") var currentShortTermPosition: Int = -1,
    @ColumnInfo(name = "previous_short_position") var previousShortTermPosition: Int = -1,
    @ColumnInfo(name = "current_mid_position") var currentMidTermPosition: Int = -1,
    @ColumnInfo(name = "previous_mid_position") var previousMidTermPosition: Int = -1,
    @ColumnInfo(name = "current_long_position") var currentLongTermPosition: Int = -1,
    @ColumnInfo(name = "previous_long_position") var previousLongTermPosition: Int = -1,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "updated_date") val updateDate: Long,
    @ColumnInfo val uri: String,
)
