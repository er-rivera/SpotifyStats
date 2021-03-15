package com.erivera.apps.topcharts.repository

import com.erivera.apps.topcharts.repository.models.api.AlbumResponse
import com.erivera.apps.topcharts.repository.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.repository.models.api.AudioFeaturesResponse
import com.erivera.apps.topcharts.repository.models.api.TrackRetrofit
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun saveSpotifyClientId(id: String)

    suspend fun hasValidSpotifyClientId(): Boolean

    suspend fun getLongTermArtists(): Flow<List<ArtistsRetrofit>>

    suspend fun getMediumTermArtists(): Flow<List<ArtistsRetrofit>>

    suspend fun getShortTermArtists(): Flow<List<ArtistsRetrofit>>

    suspend fun getLongTermTracks(): Flow<List<TrackRetrofit>>

    suspend fun getMediumTermTracks(): Flow<List<TrackRetrofit>>

    suspend fun getShortTermTracks(): Flow<List<TrackRetrofit>>

    suspend fun getAudioFeatures(trackId: String): AudioFeaturesResponse

    fun startService()

    suspend fun getAlbum(albumId: String): AlbumResponse
}