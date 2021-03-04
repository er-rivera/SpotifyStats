package com.erivera.apps.topcharts.repository

import com.erivera.apps.topcharts.models.api.*
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    fun startSpotifyService(clientId: String)

    suspend fun getTracks(limit: String, termLength: String): Flow<List<TrackRetrofit>>

    suspend fun getArtists(limit: String, termLength: String): Flow<List<ArtistsRetrofit>>

    suspend fun getAlbum(albumId: String): AlbumResponse

    suspend fun getAudioFeatures(trackId: String): AudioFeaturesResponse

    suspend fun hasValidToken(): Boolean
}