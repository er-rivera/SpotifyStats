package com.erivera.apps.topcharts.repository

import com.erivera.apps.topcharts.models.api.AlbumResponse
import com.erivera.apps.topcharts.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.models.api.AudioFeaturesResponse
import com.erivera.apps.topcharts.models.api.TrackRetrofit

interface Repository {
    fun saveSpotifyClientId(id: String)

    suspend fun hasValidSpotifyClientId(): Boolean

    suspend fun getLongTermArtists(): List<ArtistsRetrofit>

    suspend fun getMediumTermArtists(): List<ArtistsRetrofit>

    suspend fun getShortTermArtists(): List<ArtistsRetrofit>

    suspend fun getLongTermTracks(): List<TrackRetrofit>

    suspend fun getMediumTermTracks(): List<TrackRetrofit>

    suspend fun getShortTermTracks(): List<TrackRetrofit>

    suspend fun getAudioFeatures(trackId: String): AudioFeaturesResponse

    fun startService()

    suspend fun getAlbum(albumId: String): AlbumResponse
}