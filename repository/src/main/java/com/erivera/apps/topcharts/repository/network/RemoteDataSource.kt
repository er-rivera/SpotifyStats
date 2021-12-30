package com.erivera.apps.topcharts.repository.network

import com.erivera.apps.topcharts.repository.models.api.AlbumResponse
import com.erivera.apps.topcharts.repository.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.repository.models.api.AudioFeaturesResponse
import com.erivera.apps.topcharts.repository.models.api.TrackRetrofit
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    fun startSpotifyService(clientId: String)

    suspend fun getTracks(limit: String, termLength: String): Flow<List<TrackRetrofit>>

    suspend fun getArtists(limit: String, termLength: String): Flow<List<ArtistsRetrofit>>

    suspend fun getAlbum(albumId: String): AlbumResponse

    suspend fun getAudioFeatures(trackId: String): AudioFeaturesResponse

    suspend fun hasValidToken(): Boolean
}
