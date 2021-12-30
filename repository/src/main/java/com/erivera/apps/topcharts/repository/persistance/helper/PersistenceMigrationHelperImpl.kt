package com.erivera.apps.topcharts.repository.persistance.helper

import android.util.Log
import com.erivera.apps.topcharts.repository.keyvalue.DataStore
import com.erivera.apps.topcharts.repository.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.repository.models.api.TrackRetrofit
import com.erivera.apps.topcharts.repository.persistance.artist.Artist
import com.erivera.apps.topcharts.repository.persistance.artist.ArtistDatabase
import com.erivera.apps.topcharts.repository.persistance.tracks.Track
import com.erivera.apps.topcharts.repository.persistance.tracks.TrackDatabase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.util.Date
import java.util.Calendar
import java.util.concurrent.TimeUnit

class PersistenceMigrationHelperImpl(
    private val dataStore: DataStore,
    private val artistDatabase: ArtistDatabase,
    private val trackDatabase: TrackDatabase,
    private val DAYS_REQUIRED_TO_REFRESH: Int = 5,
    private val PREVIOUS_DATE_KEY: String = "PREVIOUS_DATE_KEY"
) : PersistenceSynchronizationHelper {
    companion object {
        private val TAG = PersistenceMigrationHelperImpl::class.java.name
    }

    override fun shouldUpdatePersistence(): Boolean {
        val storedDate = dataStore.getLong(PREVIOUS_DATE_KEY)
        return if (storedDate != 0L) {
            val previousDate = Date().apply {
                time = storedDate
            }
            val currentDate = Calendar.getInstance().time
            shouldUpdate(previousDate = previousDate, currentDate = currentDate)
        } else {
            true
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @FlowPreview
    override suspend fun updatePersistenceTables(
        localDbArtists: Flow<List<Artist>>,
        shortArtists: Flow<List<ArtistsRetrofit>>,
        midArtists: Flow<List<ArtistsRetrofit>>,
        longArtists: Flow<List<ArtistsRetrofit>>,
        localDbTracks: Flow<List<Track>>,
        shortTracks: Flow<List<TrackRetrofit>>,
        midTracks: Flow<List<TrackRetrofit>>,
        longTracks: Flow<List<TrackRetrofit>>
    ) {
        val currentTime = Date()
        combine(localDbArtists, shortArtists, midArtists, longArtists) { local, short, mid, long ->
            val map = buildMap {
                buildMapForArtistDb(this, local, short, mid, long, currentTime)
            }
            map.values.toList()
        }.collect { artists ->
            artistDatabase.getArtistDao().addArtists(artists)
            storeDbDate(currentTime)
        }
    }

    private fun buildMapForArtistDb(
        map: MutableMap<String, Artist>,
        prev: List<Artist>,
        short: List<ArtistsRetrofit>,
        mid: List<ArtistsRetrofit>,
        long: List<ArtistsRetrofit>,
        updatedTime: Date
    ) {
        prev.forEach {
            map[it.id] = it
            it.previousShortTermPosition = it.currentShortTermPosition
            it.currentShortTermPosition = -1
            it.previousMidTermPosition = it.currentMidTermPosition
            it.currentMidTermPosition = -1
            it.previousLongTermPosition = it.currentLongTermPosition
            it.currentLongTermPosition = -1
        }
        short.forEachIndexed { index, it ->
            val artist = Artist(
                id = it.id.orEmpty(),
                name = it.name.orEmpty(),
                type = it.type.orEmpty(),
                uri = it.uri.orEmpty(),
                updatedDate = updatedTime.time,
                currentShortTermPosition = index + 1,
                images = it.images?.first()?.url.orEmpty()
            )
            val item = map[artist.id]?.also {
                it.currentShortTermPosition = index + 1
            } ?: artist
            map[artist.id] = item
        }
        mid.forEachIndexed { index, it ->
            val artist = Artist(
                id = it.id.orEmpty(),
                name = it.name.orEmpty(),
                type = it.type.orEmpty(),
                uri = it.uri.orEmpty(),
                updatedDate = updatedTime.time,
                currentMidTermPosition = index + 1,
                images = it.images?.first()?.url.orEmpty()
            )
            val item = map[artist.id]?.also {
                it.currentMidTermPosition = index + 1
            } ?: artist
            map[artist.id] = item
        }
        long.forEachIndexed { index, it ->
            val artist = Artist(
                id = it.id.orEmpty(),
                name = it.name.orEmpty(),
                type = it.type.orEmpty(),
                uri = it.uri.orEmpty(),
                updatedDate = updatedTime.time,
                currentLongTermPosition = index + 1,
                images = it.images?.first()?.url.orEmpty()
            )
            val item = map[artist.id]?.also {
                it.currentLongTermPosition = index + 1
            } ?: artist
            map[artist.id] = item
        }
    }

    private fun storeDbDate(date: Date) {
        dataStore.setLong(PREVIOUS_DATE_KEY, date.time)
    }

    private fun shouldUpdate(previousDate: Date, currentDate: Date): Boolean {
        val current = currentDate.time
        val previous = previousDate.time
        val differenceInMillis = previous - current
        Log.d(TAG, "differenceInMillis $differenceInMillis")
        return TimeUnit.DAYS.toDays(differenceInMillis) >= DAYS_REQUIRED_TO_REFRESH
    }
}