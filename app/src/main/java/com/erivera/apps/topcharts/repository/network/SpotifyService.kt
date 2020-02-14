package com.erivera.apps.topcharts.repository.network

import com.erivera.apps.topcharts.models.api.AlbumResponse
import com.erivera.apps.topcharts.models.api.ArtistResponse
import com.erivera.apps.topcharts.models.api.TrackResponse
import com.erivera.apps.topcharts.models.api.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyService {
    @GET("/v1/me/top/artists")
    suspend fun getArtists(@Query("limit") limit: String, @Query("time_range") timeRange: String): Response<ArtistResponse>

    @GET("/v1/me")
    suspend fun getUser() : Response<UserResponse>

    @GET("/v1/me/top/tracks")
    suspend fun getTracks(@Query("limit") limit: String, @Query("time_range") timeRange: String): Response<TrackResponse>

    @GET("v1/albums/{id}")
    suspend fun getAlbum(@Path("id") albumId: String): Response<AlbumResponse>
}