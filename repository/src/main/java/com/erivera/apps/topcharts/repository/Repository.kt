package com.erivera.apps.topcharts.repository

import com.erivera.apps.topcharts.repository.models.api.AlbumResponse
import com.erivera.apps.topcharts.repository.models.api.AudioFeaturesResponse
import com.erivera.apps.topcharts.repository.persistance.artist.Artist
import com.erivera.apps.topcharts.repository.persistance.tracks.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface Repository {
    fun saveSpotifyClientId(id: String)

    suspend fun hasValidSpotifyClientId(): Boolean

    suspend fun getLongTermArtists(): Flow<List<Artist>>

    suspend fun getMediumTermArtists(): Flow<List<Artist>>

    suspend fun getShortTermArtists(): Flow<List<Artist>>

    suspend fun getLongTermTracks(): Flow<List<Track>>

    suspend fun getMediumTermTracks(): Flow<List<Track>>

    suspend fun getShortTermTracks(): Flow<List<Track>>

    suspend fun getAudioFeatures(trackId: String): AudioFeaturesResponse

    fun startService()

    suspend fun getAlbum(albumId: String): AlbumResponse

    suspend fun refreshDb(refreshFlow: MutableStateFlow<Boolean>)
}