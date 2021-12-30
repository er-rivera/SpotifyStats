package com.erivera.apps.topcharts.repository.persistance.artist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val type: String,
    @ColumnInfo val uri: String,
    @ColumnInfo(name = "updated_date") val updatedDate: Long,
    @ColumnInfo(name = "current_short_position") var currentShortTermPosition: Int = -1,
    @ColumnInfo(name = "previous_short_position") var previousShortTermPosition: Int = -1,
    @ColumnInfo(name = "current_mid_position") var currentMidTermPosition: Int = -1,
    @ColumnInfo(name = "previous_mid_position") var previousMidTermPosition: Int = -1,
    @ColumnInfo(name = "current_long_position") var currentLongTermPosition: Int = -1,
    @ColumnInfo(name = "previous_long_position") var previousLongTermPosition: Int = -1,
    @ColumnInfo val images: String
)
