package com.erivera.apps.topcharts.repository

import com.erivera.apps.topcharts.models.api.AlbumResponse
import com.erivera.apps.topcharts.models.api.AlbumRetrofit
import com.erivera.apps.topcharts.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.models.api.TrackRetrofit

interface RemoteDataSource {

    fun startSpotifyService(clientId: String)

    suspend fun getTracks(limit: String, termLength: String): List<TrackRetrofit>

    suspend fun getArtists(limit: String, termLength: String): List<ArtistsRetrofit>

    suspend fun getAlbum(albumId: String): AlbumResponse

    suspend fun hasValidToken(): Boolean
}