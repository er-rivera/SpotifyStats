package com.erivera.apps.topcharts.repository.persistance.helper

import com.erivera.apps.topcharts.repository.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.repository.models.api.TrackRetrofit
import com.erivera.apps.topcharts.repository.persistance.artist.Artist
import com.erivera.apps.topcharts.repository.persistance.tracks.Track
import kotlinx.coroutines.flow.Flow

interface PersistenceSynchronizationHelper {
    fun shouldUpdatePersistence(): Boolean

    suspend fun updatePersistenceTables(
        localDbArtists: Flow<List<Artist>>,
        shortArtists: Flow<List<ArtistsRetrofit>>,
        midArtists: Flow<List<ArtistsRetrofit>>,
        longArtists: Flow<List<ArtistsRetrofit>>,
        localDbTracks: Flow<List<Track>>,
        shortTracks: Flow<List<TrackRetrofit>>,
        midTracks: Flow<List<TrackRetrofit>>,
        longTracks: Flow<List<TrackRetrofit>>
    )
}