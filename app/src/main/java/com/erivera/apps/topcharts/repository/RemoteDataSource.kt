package com.erivera.apps.topcharts.repository

import com.erivera.apps.topcharts.models.api.*

interface RemoteDataSource {

    fun startSpotifyService(clientId: String)

    suspend fun getTracks(limit: String, termLength: String): List<TrackRetrofit>

    suspend fun getArtists(limit: String, termLength: String): List<ArtistsRetrofit>

    suspend fun getAlbum(albumId: String): AlbumResponse

    suspend fun getAudioFeatures(trackId: String): AudioFeaturesResponse

    suspend fun hasValidToken(): Boolean
}